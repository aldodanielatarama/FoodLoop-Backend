/* FoodLoop portal real: cada operación utiliza la API REST de Spring Boot.
   El JWT permanece en sessionStorage. No se admiten pagos reales ni rastreo GPS simulado. */
'use strict';

const $ = selector => document.querySelector(selector);
const escapeHTML = value => String(value ?? '').replace(/[&<>"']/g,
  char => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[char]));
const fmtMoney = n => 'S/ ' + Number(n || 0).toFixed(2);
const fmtTime = t => t ? new Date(t).toLocaleString('es-PE', { dateStyle:'short', timeStyle:'short' }) : 'Sin fecha';
const futureLocal = hours => {
  const d = new Date(Date.now() + hours * 3600000);
  const f = n => String(n).padStart(2,'0');
  return `${d.getFullYear()}-${f(d.getMonth()+1)}-${f(d.getDate())}T${f(d.getHours())}:${f(d.getMinutes())}`;
};
let auth = JSON.parse(sessionStorage.getItem('foodloop-session') || 'null');
let profile = null;
let catalog = [];
let myShops = [];
let view = 'catalogo';

async function api(path, options = {}) {
  const headers = { ...(options.body ? {'Content-Type':'application/json'} : {}), ...(options.headers || {}) };
  if (auth?.token) headers.Authorization = `Bearer ${auth.token}`;
  let response;
  try { response = await fetch(path, { ...options, headers }); }
  catch { throw Error('No se pudo conectar con Spring Boot. Comprueba que el servidor esté ejecutándose.'); }
  const text = await response.text();
  let body;
  try { body = text ? JSON.parse(text) : null; } catch { body = text; }
  if (!response.ok) {
    if (response.status === 401 && auth && !path.startsWith('/auth/session')) logout(false);
    const error = body?.error || body?.detail || body?.message || body?.title || text || 'Operación rechazada';
    throw Error(`${error} (HTTP ${response.status})`);
  }
  return body;
}
const json = (path, obj, method = 'POST') => api(path, { method, body:JSON.stringify(obj) });
const post = path => api(path, { method:'POST' });

function notify(message, isError=false) {
  const box = $('#feedback'); box.textContent = message;
  box.className = 'feedback' + (isError ? ' err' : '');
  box.style.display = 'block';
  window.scrollTo({ top:150, behavior:'smooth' });
}
async function execute(action, success='Operación completada') {
  try { await action(); notify(success); await refresh(); }
  catch (error) { notify(error.message, true); }
}
function modal(markup, onSubmit) {
  $('#dialogBody').innerHTML = markup;
  const dlg = $('#dialog');
  dlg.showModal();
  const form = dlg.querySelector('form');
  if (form && onSubmit) form.addEventListener('submit', async e => {
    e.preventDefault();
    const btn = form.querySelector('[type="submit"]'); if(btn) btn.disabled = true;
    try { await onSubmit(new FormData(form)); dlg.close(); await refresh(); }
    catch(error) { notify(error.message,true); }
    finally { if(btn) btn.disabled = false; }
  });
}
const field = (title,name,type='text',required=true,extra='') => `<label>${escapeHTML(title)}<input name="${name}" type="${type}" ${required?'required':''} ${extra}></label>`;
const saveButton = label => `<div class="full"><button class="btn primary" type="submit">${label}</button></div>`;
function authenticateDialog(register=false) {
  modal(`<p class="eyebrow">ACCESO A FOODLOOP</p><h2>${register?'Crear una cuenta':'Iniciar sesión'}</h2>
    <p>${register?'Selecciona el perfil que usarás. Las organizaciones necesitan verificación administrativa.':'Usa una cuenta de demostración o una cuenta registrada en PostgreSQL.'}</p>
    <form class="formgrid">
      ${register?field('Nombre o razón de contacto','nombreCompleto'):''}
      ${field('Usuario','username')}
      ${field('Contraseña (mínimo 8 caracteres)','password','password')}
      ${register?`<label>Tipo de cuenta<select name="rol"><option value="COMPRADOR">Comprador</option><option value="VENDEDOR">Comercio vendedor/donante</option><option value="ORGANIZACION">Organización beneficiaria</option></select></label>`:''}
      ${saveButton(register?'Registrarme':'Entrar')}
    </form>
    <p><button class="btn light" id="toggleAuth">${register?'Ya tengo cuenta':'Crear nueva cuenta'}</button></p>`,
    async data => {
      const credentials = Object.fromEntries(data);
      if(register) {
        await json('/auth/register',credentials);
        notify('Cuenta creada. Ahora inicia sesión; las organizaciones requieren verificación.');
        return;
      }
      auth = await json('/auth/session',credentials);
      sessionStorage.setItem('foodloop-session',JSON.stringify(auth));
      notify(`Sesión iniciada como ${auth.username}`);
    });
  $('#toggleAuth').addEventListener('click',()=>{ $('#dialog').close(); authenticateDialog(!register); });
}
function logout(render=true) {
  auth=null; profile=null; sessionStorage.removeItem('foodloop-session');
  if(render) { refresh(); notify('Sesión cerrada'); }
}
function setSessionUI(){
  $('#sessionArea').innerHTML = profile ? `<div class="row"><span class="muted">${escapeHTML(profile.username)} · ${escapeHTML(profile.rol)}</span><button class="btn outline small" id="logoutBtn">Salir</button></div>`
    : '<button class="btn primary" id="loginBtn">Iniciar sesión</button>';
  const logoutBtn = $('#logoutBtn'), loginBtn = $('#loginBtn');
  if(logoutBtn) logoutBtn.onclick=()=>logout();
  if(loginBtn) loginBtn.onclick=()=>authenticateDialog(false);
  $('#currentRole').textContent = profile ? `Conectado: ${profile.rol}` : 'Modo visitante';
}
function viewScreen(name) {
  view = name;
  document.querySelectorAll('.screen').forEach(node=>node.classList.toggle('active',node.id==='view-'+name));
  document.querySelectorAll('[data-view]').forEach(node=>node.classList.toggle('selected',node.dataset.view===name));
  if(name==='panel') loadPanel();
  if(name==='operaciones') loadOperations();
  if(name==='avisos') loadNotices();
}
function iconFor(cat='') {
  let v=cat.toLowerCase();
  return v.includes('pan')?'🥖':v.includes('fruta')?'🍎':v.includes('preparada')?'🥗':'🍲';
}
function displayCatalog(){
  const q=$('#q').value.toLowerCase(), d=$('#district').value.toLowerCase(), mode=$('#mode').value;
  const shown=catalog.filter(item=>(!q||(item.nombre||'').toLowerCase().includes(q))&&(!d||(item.distrito||'').toLowerCase().includes(d))&&(!mode||mode===item.modalidad));
  $('#catalogGrid').innerHTML = shown.length ? shown.map(l=>`<article class="card">
    <div class="art">${l.imagenUrl?`<img src="${escapeHTML(l.imagenUrl)}" alt="${escapeHTML(l.nombre)}" loading="lazy" onerror="this.hidden=true">`:iconFor(l.categoria)}<span class="pill">${l.modalidad==='DONACION'?'Donación solidaria':'Oferta con descuento'}</span></div>
    <div class="cardcontent"><div class="meta">📍 ${escapeHTML(l.distrito||'Lima')} · ${escapeHTML(l.comercio||'Comercio')}</div><h3>${escapeHTML(l.nombre)}</h3>
      <span class="muted">${escapeHTML(l.categoria)} · ${escapeHTML(l.conservacion)}</span>
      <div class="money">${l.modalidad==='DONACION'?'GRATIS':fmtMoney(l.precioLiquidacion)} ${l.modalidad==='VENTA'?`<s>${fmtMoney(l.precioOriginal)}</s>`:''}</div>
      <span class="muted">${l.stockDisponible} unidades · Reserva hasta ${fmtTime(l.limiteReserva)}</span>
      <div class="cardfooter"><span class="badge">Recojo ${fmtTime(l.limiteRecojo)}</span><button class="btn primary small" data-lote="${l.id}">Detalles</button></div>
    </div></article>`).join('') : '<div class="empty">No hay lotes disponibles con esos filtros.</div>';
  document.querySelectorAll('[data-lote]').forEach(btn=>btn.onclick=()=>showDetail(Number(btn.dataset.lote)));
}
function showDetail(id) {
  const l=catalog.find(x=>x.id===id);
  if(!l) return;
  modal(`<p class="eyebrow">LOTE #${id}</p><h2>${escapeHTML(l.nombre)}</h2>
    <p>${escapeHTML(l.descripcion||'Excedentes publicados por el establecimiento.')}</p>
    <div class="panel"><p><b>${escapeHTML(l.comercio)}</b> · ${escapeHTML(l.distrito||'')} · ${escapeHTML(l.direccion||'')}</p>
    <p><b>Stock:</b> ${l.stockDisponible} · <b>Precio:</b> ${l.modalidad==='DONACION'?'Donación':fmtMoney(l.precioLiquidacion)}</p>
    <p><b>Alérgenos:</b> ${escapeHTML(l.alergenos||'No declarados')}</p>
    <p><b>Conservación:</b> ${escapeHTML(l.conservacion||'Sin datos')} · <b>Vence:</b> ${fmtTime(l.fechaVencimiento)}</p>
    <p><b>Fin de reservas:</b> ${fmtTime(l.limiteReserva)} · <b>Recojo máximo:</b> ${fmtTime(l.limiteRecojo)}</p>
    </div><p>⚠ La plataforma registra datos declarados. El comercio es responsable de la inocuidad; no se realiza cobro en línea.</p>
    ${l.latitud&&l.longitud?`<button type="button" class="btn outline" id="routeEstimate">Consultar tiempo de viaje real (requiere permiso GPS)</button><div id="routeResult" class="muted"></div>`:''}
    <form class="formgrid">${field('Unidades solicitadas','cantidad','number',true,`min="1" max="${l.stockDisponible}" value="1"`)}
      <label class="full">Comentario (opcional)<textarea name="comentario" maxlength="400"></textarea></label>
      ${saveButton(l.modalidad==='DONACION'?'Enviar solicitud de donación':'Reservar sin pago online')}
    </form>`, async values=>{
      if(!profile){throw Error('Primero debes iniciar sesión desde el botón superior');}
      if(l.modalidad==='DONACION'&&profile.rol!=='ORGANIZACION')throw Error('Debes iniciar sesión como organización verificada');
      if(l.modalidad==='VENTA'&&profile.rol!=='COMPRADOR')throw Error('Debes iniciar sesión como comprador');
      const cant=Number(values.get('cantidad'));
      if(!Number.isInteger(cant)||cant<1||cant>l.stockDisponible)throw Error('Cantidad fuera del stock disponible');
      await json('/api/foodloop/solicitudes',{loteId:id,cantidad:cant,comentario:values.get('comentario')});
      notify(l.modalidad==='DONACION'?'Solicitud enviada para evaluación del comercio':'Reserva creada. Consulta el código de retiro en Mis solicitudes.');
      viewScreen('operaciones');
    });
  const routeBtn=$('#routeEstimate');
  if(routeBtn) routeBtn.onclick=()=>{
    if(!navigator.geolocation){$('#routeResult').textContent='Navegador sin geolocalización';return;}
    $('#routeResult').textContent='Consultando ubicación autorizada...';
    navigator.geolocation.getCurrentPosition(async position=>{
      try{
        const query=new URLSearchParams({origenLat:position.coords.latitude,origenLon:position.coords.longitude,
          destinoLat:l.latitud,destinoLon:l.longitud});
        const r=await api('/api/foodloop/rutas/eta?'+query);
        const route=r.resultado?.routes?.[0];
        $('#routeResult').textContent=route?`Distancia ${Math.round(route.distanceMeters/100)/10} km · Duración estimada ${route.duration}`:
          r.mensaje||'No hay ruta disponible';
      }catch(e){$('#routeResult').textContent=e.message;}
    },()=>$('#routeResult').textContent='Ubicación no autorizada. No se calcula ETA.');
  };
}
const guest = text => `<div class="empty">${text}<p><button class="btn primary" id="promptLogin">Iniciar sesión</button></p></div>`;
function promptLogin(){let button=$('#promptLogin');if(button)button.onclick=()=>authenticateDialog(false);}
function badge(status) {return `<span class="badge ${['CANCELADA','RECHAZADA','VENCIDO'].includes(status)?'danger':['PENDIENTE','PARCIAL_OFRECIDA'].includes(status)?'pending':''}">${escapeHTML(status)}</span>`;}
const action = (label,name,id,value='')=>`<button class="btn small light" data-action="${name}" data-id="${id}" data-value="${escapeHTML(value)}">${label}</button>`;
function requestCard(s, seller=false){
  const l=catalog.find(x=>x.id===s.loteId);
  return `<article class="listcard"><div><h4>Solicitud #${s.id} · Lote #${s.loteId}</h4>
    <p>${escapeHTML(l?.nombre || s.tipo)} · ${s.cantidadSolicitada} solicitadas · ${s.cantidadAsignada} asignadas · ${badge(s.estado)}</p>
    <p>${s.codigoRetiro?`Código de recojo: <strong>${escapeHTML(s.codigoRetiro)}</strong> · `:''}${fmtTime(s.creadaEn)}</p>
    <p>Tipo: ${escapeHTML(s.tipo)}. ${s.tipo==='VENTA'?'Reserva sin cobro en línea.':''}</p></div>
    <div class="actions">
      ${seller&&s.estado==='PENDIENTE'? action('Asignar cantidad','assign',s.id):''}
      ${!seller&&s.estado==='PARCIAL_OFRECIDA'?action('Aceptar parcial','accept',s.id)+action('Rechazar','decline',s.id):''}
      ${seller&&s.estado==='ASIGNADA'?action('Listo para recojo','ready',s.id):''}
      ${seller&&s.estado==='LISTA'?action('Confirmar entrega con código','deliver',s.id):''}
      ${!seller&&['PENDIENTE','PARCIAL_OFRECIDA','ASIGNADA'].includes(s.estado)?action('Cancelar','cancel',s.id):''}
      ${['ASIGNADA','LISTA','ENTREGADA'].includes(s.estado)?action('Estado de entrega','track',s.id):''}
      ${action('Reportar problema','issue',s.id)}
    </div></article>`;
}
function bindActions(root=document){
  root.querySelectorAll('[data-action]').forEach(btn=>btn.onclick=async()=>{
    const id=Number(btn.dataset.id), what=btn.dataset.action;
    if(what==='assign') {
      const qty=prompt('¿Cuántas unidades asignas? No puede superar el stock.', '1');
      if(qty!==null)execute(()=>post(`/api/foodloop/solicitudes/${id}/asignar?cantidad=${encodeURIComponent(qty)}`),'Asignación registrada');
    } else if(what==='accept'||what==='decline') execute(()=>post(`/api/foodloop/solicitudes/${id}/aceptar-parcial?aceptar=${what==='accept'}`),'Respuesta registrada');
    else if(what==='ready') execute(()=>post(`/api/foodloop/solicitudes/${id}/lista`),'Lote listo para recojo');
    else if(what==='deliver') {
      const code=prompt('Introduce el código entregado por el comprador u organización:');
      if(code) execute(()=>post(`/api/foodloop/solicitudes/${id}/entregar?codigo=${encodeURIComponent(code)}`),'Entrega confirmada');
    } else if(what==='cancel') {if(confirm('¿Cancelar solicitud?'))execute(()=>post(`/api/foodloop/solicitudes/${id}/cancelar`),'Solicitud cancelada');}
    else if(what==='track')execute(async()=>{const e=await api(`/api/foodloop/solicitudes/${id}/entrega`);modal(`<h2>Entrega #${escapeHTML(e.id)}</h2><p>${badge(e.estado)} · ${escapeHTML(e.modalidad)}</p><p>Origen: ${escapeHTML(e.origen)}</p><p>Última actualización: ${fmtTime(e.entregadaEn||e.listaEn||e.horaEstimada)}</p><p>Sin GPS no se afirma conocer una ubicación en tiempo real. La hora indicada es el límite programado de recojo, no una ETA calculada.</p>`);},'Seguimiento consultado');
    else if(what==='issue') issueModal(id);
    else if(what==='verify')execute(()=>post(`/api/foodloop/admin/organizaciones/${id}/verificar`),'Organización verificada');
    else if(what==='resolve') {const nota=prompt('Describe cómo resolviste la incidencia:');if(nota) execute(()=>post(`/api/foodloop/admin/incidencias/${id}/resolver?nota=${encodeURIComponent(nota)}`),'Incidencia resuelta');}
    else if(what==='pause')execute(()=>api(`/api/foodloop/lotes/${id}/estado?valor=${btn.dataset.value}`,{method:'PATCH'}),'Publicación actualizada');
  });
}
async function loadOperations(){
  if(!profile){$('#operations').innerHTML=guest('Inicia sesión para consultar tus reservas y solicitudes.');promptLogin();return;}
  try {
    const list=await api('/api/foodloop/mi/solicitudes');
    $('#operations').innerHTML=list.length?list.map(s=>requestCard(s)).join(''):'<div class="empty">Aún no tienes solicitudes. Explora el catálogo para comenzar.</div>';
    bindActions($('#operations'));
  } catch(e){$('#operations').innerHTML=`<div class="empty">${escapeHTML(e.message)}</div>`;}
}
function shopDialog(){
  modal(`<p class="eyebrow">REGISTRAR COMERCIO</p><h2>Datos del establecimiento</h2><p>Los lotes deben pertenecer a un comercio registrado y a su propietario.</p>
   <form class="formgrid">${field('Nombre comercial','nombreComercial')}${field('Dirección de recojo','direccion')}
   ${field('Distrito','distrito')}${field('RUC (opcional)','ruc','text',false)}
   ${field('Horario atención','horarioAtencion','text',false)}${field('Horario recojo','horarioRecojo','text',false)}
   ${field('Latitud (opcional)','latitud','number',false,'step="any"')}${field('Longitud (opcional)','longitud','number',false,'step="any"')}
   ${saveButton('Registrar establecimiento')}</form>`,async fd=>{
     const d=Object.fromEntries(fd);for(const field of ['latitud','longitud'])d[field]=d[field]?Number(d[field]):null;
     await json('/api/foodloop/comercios',d);notify('Establecimiento creado');
   });
}
function publishDialog(){
  if(!myShops.length){shopDialog();return;}
  modal(`<p class="eyebrow">GESTIÓN DE MERMA</p><h2>Publicar lote con condiciones</h2>
  <p>El horario límite de reserva no puede superar el de recojo; y el de recojo no puede superar el vencimiento declarado.</p>
  <form class="formgrid">
    <label class="full">Establecimiento<select name="establecimientoId">${myShops.map(e=>`<option value="${e.id}">${escapeHTML(e.nombreComercial)}</option>`).join('')}</select></label>
    ${field('Alimento','nombre')}${field('Categoría','categoria')}
    <label>Modalidad<select name="modalidad"><option value="VENTA">Venta</option><option value="DONACION">Donación</option></select></label>
    ${field('Unidades','cantidad','number',true,'min="1" value="10"')}
    ${field('Precio de liquidación','precioLiquidacion','number',false,'min="0" step="0.01" value="6"')}
    ${field('Precio original','precioOriginal','number',false,'min="0" step="0.01" value="12"')}
    ${field('Peso por unidad kg','pesoPorUnidadKg','number',false,'step="0.01" min="0" value="0.25"')}
    <label>Conservación<select name="conservacion"><option>Ambiente protegido</option><option>Refrigerado</option><option>Congelado</option><option>Ambiente fresco</option></select></label>
    ${field('Alérgenos','alergenos','text',true,'placeholder="Ninguno declarado / detalle"')}
    ${field('Fin de reservas','limiteReserva','datetime-local',true,`value="${futureLocal(2)}"`)}
    ${field('Límite de recojo','limiteRecojo','datetime-local',true,`value="${futureLocal(4)}"`)}
    ${field('Vencimiento declarado','fechaVencimiento','datetime-local',true,`value="${futureLocal(7)}"`)}
    ${field('URL de imagen (opcional)','imagenUrl','url',false)}
    <label class="full">Descripción<textarea name="descripcion" maxlength="400" placeholder="Preparación, empaque, procedencia"></textarea></label>
    ${saveButton('Publicar excedente')}
  </form>`,async fd=>{
    const d=Object.fromEntries(fd);
    for(const n of ['establecimientoId','cantidad','precioLiquidacion','precioOriginal','pesoPorUnidadKg'])d[n]=d[n]?Number(d[n]):null;
    if(d.modalidad==='DONACION')d.precioLiquidacion=0;
    d.politicaAsignacion='MANUAL';
    await json('/api/foodloop/lotes',d);notify('Lote publicado; stock y fechas registrados');
  });
}
function orgDialog(){
  modal(`<p class="eyebrow">PERFIL SOLIDARIO</p><h2>Registro de organización</h2>
    <p>La cuenta necesita validación administrativa antes de solicitar donaciones.</p>
    <form class="formgrid">${field('Nombre / razón social','razonSocial')}${field('Documento / identificación','identificacion')}
    ${field('Dirección','direccion')}${field('Distrito','distrito')}
    ${field('Raciones que puede recibir','capacidadRaciones','number',true,'min="1" value="20"')}${field('Contacto','contacto','text',false)}
    <label>Transporte propio<select name="transportePropio"><option value="false">No</option><option value="true">Sí</option></select></label>
    <label>Refrigeración disponible<select name="refrigeracion"><option value="false">No</option><option value="true">Sí</option></select></label>
    ${saveButton('Guardar perfil')}</form>`,async fd=>{
      const d=Object.fromEntries(fd);d.capacidadRaciones=Number(d.capacidadRaciones);
      d.transportePropio=d.transportePropio==='true';d.refrigeracion=d.refrigeracion==='true';
      await json('/api/foodloop/organizaciones/perfil',d);notify('Perfil guardado. Espera verificación administrativa.');
    });
}
function issueModal(id=null){
  modal(`<p class="eyebrow">SOPORTE</p><h2>Reportar incidencia</h2>
   <form class="formgrid"><label>Tipo<select name="tipo"><option>Demora de recojo</option><option>Alimento deteriorado</option><option>Cantidad incorrecta</option><option>Local cerrado</option><option>Otro</option></select></label>
   <label class="full">Descripción<textarea name="descripcion" required maxlength="1000"></textarea></label>
   ${saveButton('Enviar reporte')}</form>`,async fd=>{
     const d=Object.fromEntries(fd);d.solicitudId=id;
     await json('/api/foodloop/incidencias',d);notify('Incidencia registrada');
   });
}
async function loadPanel(){
  const root=$('#rolePanel');
  if(!profile){root.innerHTML=guest('Para publicar, donar o administrar necesitas una cuenta.');promptLogin();return;}
  try {
    if(profile.rol==='VENDEDOR'){
      const [shops,lots,requests,metrics,forecast]=await Promise.all([
        api('/api/foodloop/mi/comercios'),api('/api/foodloop/mi/lotes'),api('/api/foodloop/comercio/solicitudes'),
        api('/api/foodloop/comercio/metricas'),api('/api/foodloop/comercio/estimacion')]);
      myShops=shops;
      root.innerHTML=`<div class="stats"><div class="stat"><b>${metrics.lotesPublicados}</b><small>Lotes publicados</small></div><div class="stat"><b>${metrics.solicitudes}</b><small>Solicitudes recibidas</small></div><div class="stat"><b>${metrics.unidadesEntregadas}</b><small>Unidades confirmadas</small></div><div class="stat"><b>${fmtMoney(metrics.ingresosRecuperados)}</b><small>Ingresos de reservas entregadas (sin validar pago)</small></div></div>
        <div class="panel"><div class="row"><div><h3>Mis establecimientos</h3><p class="muted">${shops.map(e=>escapeHTML(e.nombreComercial)).join(', ')||'No tienes establecimientos'}</p></div><button class="btn outline" id="newShop">+ Registrar establecimiento</button></div></div>
        <div class="panel"><div class="row"><h3>Mis lotes y disponibilidad</h3><button class="btn primary" id="newLot">+ Publicar excedente</button></div>
          ${lots.map(l=>`<div class="listcard"><div><b>${escapeHTML(l.nombre)}</b> ${badge(l.estado)}<p>${l.modalidad} · ${l.stockDisponible} libres / ${l.stockReservado} reservadas · Vence ${fmtTime(l.fechaVencimiento)}</p></div><div class="actions">${action(l.estado==='PAUSADO'?'Reactivar':'Pausar','pause',l.id,l.estado==='PAUSADO'?'ACTIVO':'PAUSADO')}</div></div>`).join('')||'<p class="muted">Aún no publicaste lotes.</p>'}</div>
        <div class="panel"><h3>Solicitudes recibidas y entregas</h3>${requests.map(s=>requestCard(s,true)).join('')||'<p class="muted">Aún no hay solicitudes.</p>'}</div>
        <div class="panel"><h3>Estimación basada en histórico</h3><p class="muted">${escapeHTML(forecast.disponible?`Promedio por operación entregada: ${forecast.promedioUnidadesPorVenta} unidades (${forecast.muestra} observaciones). Estadística sencilla, no un modelo IA entrenado.`:forecast.mensaje)}</p></div>`;
      $('#newShop').onclick=shopDialog;$('#newLot').onclick=publishDialog;bindActions(root);
    } else if(profile.rol==='ORGANIZACION'){
      const [org,requests]=await Promise.all([api('/api/foodloop/organizaciones/mi-perfil'),api('/api/foodloop/mi/solicitudes')]);
      root.innerHTML=`<div class="panel"><div class="row"><h3>Organización beneficiaria</h3><button id="editOrg" class="btn primary">${org?'Actualizar perfil':'Registrar organización'}</button></div>
        <p>${org?`${escapeHTML(org.razonSocial)} · ${org.verificada?'Verificada ✅':'Pendiente de verificación ⏳'} · Capacidad ${org.capacidadRaciones||'No definida'}`:'Completa los datos de la organización antes de solicitar donaciones.'}</p>
        <p class="muted">La asignación puede ser parcial. Debes contar con conservación y transporte compatibles.</p></div>
        <div class="panel"><h3>Solicitudes de donación</h3>${requests.map(s=>requestCard(s)).join('')||'<p class="muted">Todavía no has solicitado donaciones.</p>'}</div>`;
      $('#editOrg').onclick=orgDialog;bindActions(root);
    } else if(profile.rol==='ADMIN'){
      const [pending,allIssues]=await Promise.all([api('/api/foodloop/admin/organizaciones'),api('/api/foodloop/admin/incidencias')]);
      root.innerHTML=`<div class="panel"><h3>Verificación de organizaciones</h3><p class="muted">Antes de aprobar, revisa su identidad y capacidad fuera de la plataforma.</p>
        ${pending.map(o=>`<div class="listcard"><div><b>${escapeHTML(o.razonSocial)}</b><p>${escapeHTML(o.direccion)} · ${escapeHTML(o.identificacion)}</p></div>${action('Marcar verificada','verify',o.id)}</div>`).join('')||'<p class="muted">No hay organizaciones pendientes de revisión.</p>'}</div>
      <div class="panel"><h3>Incidencias para revisar</h3>${allIssues.map(i=>`<div class="listcard"><div><b>${escapeHTML(i.tipo||'Incidencia')} #${i.id}</b><p>${escapeHTML(i.descripcion)}</p>${badge(i.estado)}</div>${i.estado==='ABIERTA'?action('Resolver','resolve',i.id):''}</div>`).join('')||'<p class="muted">No hay incidencias.</p>'}</div>`;
      bindActions(root);
    } else {
      const ss=await api('/api/foodloop/mi/solicitudes');
      root.innerHTML=`<div class="panel"><h3>Mi actividad como comprador</h3><p>Reservas activas y códigos de recojo.</p>${ss.map(s=>requestCard(s)).join('')||'<p class="muted">Todavía no tienes reservas.</p>'}</div>`;
      bindActions(root);
    }
  } catch(e){root.innerHTML=`<div class="empty">${escapeHTML(e.message)}</div>`;}
}
async function loadNotices(){
  if(!profile){$('#notifications').innerHTML=guest('Inicia sesión para consultar notificaciones.');$('#issues').innerHTML='';promptLogin();return;}
  try {
    const [notifications,issues]=await Promise.all([api('/api/foodloop/mi/notificaciones'),api('/api/foodloop/mi/incidencias')]);
    $('#notifications').innerHTML=notifications.length?notifications.map(n=>`<div class="listcard"><div><b>${escapeHTML(n.titulo)}</b><p>${escapeHTML(n.mensaje)}</p><small class="muted">${fmtTime(n.fecha)}</small></div></div>`).join(''):'<div class="empty">Sin notificaciones aún.</div>';
    $('#issues').innerHTML=`<p><button id="newIssue" class="btn outline">Reportar otra incidencia</button></p>`+
      (issues.length?issues.map(i=>`<div class="listcard"><div><b>${escapeHTML(i.tipo)} #${i.id}</b>${badge(i.estado)}<p>${escapeHTML(i.descripcion)}</p><p>${escapeHTML(i.resolucion||'Pendiente de revisión')}</p></div></div>`).join(''):'<div class="empty">No se han registrado incidencias.</div>');
    $('#newIssue').onclick=()=>issueModal(null);
  }catch(e){$('#notifications').textContent=e.message;}
}
async function refresh(){
  try {
    catalog=await api('/api/foodloop/lotes');
    $('#connectionStatus').textContent='Conectado con API Java + PostgreSQL';
  } catch(e){catalog=[];$('#connectionStatus').textContent='No se puede acceder a la API. Revisa la configuración.';notify(e.message,true);}
  if(auth?.token){
    try {profile=await api('/auth/me');}
    catch {profile=null;}
  } else profile=null;
  setSessionUI();displayCatalog();
  if(view==='operaciones') await loadOperations();
  if(view==='panel') await loadPanel();
  if(view==='avisos') await loadNotices();
}
document.querySelectorAll('[data-view]').forEach(button=>button.addEventListener('click',()=>viewScreen(button.dataset.view)));
for(const input of ['q','district','mode']) $( '#'+input ).addEventListener('input',displayCatalog);
$('#reloadCatalog').onclick=refresh;
$('#dialogClose').onclick=()=>$('#dialog').close();
$('#dialog').addEventListener('click',e=>{if(e.target===$('#dialog'))$('#dialog').close();});
refresh();
