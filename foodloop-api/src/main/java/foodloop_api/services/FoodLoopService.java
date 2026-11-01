package foodloop_api.services;

import foodloop_api.models.*;
import foodloop_api.dto.*;
import foodloop_api.repositories.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service 
public class FoodLoopService {
 private final UsuarioRepository usuarios;
 private final EstablecimientoRepository establecimientos;
 private final ProductoRepository productos;
 private final LoteExcedenteRepository lotes;
 private final SolicitudRepository solicitudes;
 private final EntregaRepository entregas;
 private final NotificacionRepository notificaciones;
 private final MovimientoInventarioRepository movimientos;
 private final HistorialConservacionRepository conservaciones;
 private final OrganizacionBeneficiariaRepository organizaciones;
 private final IncidenciaRepository incidencias;
 private final SecureRandom aleatorio=new SecureRandom();

 public Usuario actual(String username) {
     if (username == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Debes iniciar sesión");
     Usuario u=usuarios.findByUsername(username);
     if(u==null || "SUSPENDIDO".equals(u.getEstadoCuenta())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Cuenta no disponible");
     return u;
 }
 private void rol(Usuario u,String requerido) {
     if(!requerido.equals(u.getRol()) && !"ADMIN".equals(u.getRol())) throw new AccessDeniedException("Requiere rol "+requerido);
 }
 private void dueño(Long propietario,Usuario u) {
     if(!Objects.equals(propietario,u.getId()) && !"ADMIN".equals(u.getRol())) throw new AccessDeniedException("No administras este registro");
 }
 private ResponseStatusException invalido(String mensaje) { return new ResponseStatusException(HttpStatus.BAD_REQUEST,mensaje); }
 private LoteExcedente lote(Long id) { return lotes.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Lote no encontrado")); }
 private Solicitud solicitud(Long id) { return solicitudes.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Solicitud no encontrada")); }
 private LoteExcedente bloqueado(Long id) { return lotes.bloquear(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Lote no encontrado")); }
 private void avisar(Long user,String titulo,String msg) { Notificacion n=new Notificacion();n.setUsuarioId(user);n.setTitulo(titulo);n.setMensaje(msg);notificaciones.save(n); }
 private void movimiento(LoteExcedente l,Usuario u,int cantidad,String motivo) {
     MovimientoInventario m=new MovimientoInventario();m.setLoteId(l.getId());m.setUsuarioId(u.getId());m.setUnidades(cantidad);m.setMotivo(motivo);movimientos.save(m);
 }
 private String codigo(){return String.format("%06d",aleatorio.nextInt(1000000));}
 private void valido(LoteExcedente l) {
     LocalDateTime ahora=LocalDateTime.now(java.time.ZoneId.of("America/Lima"));
     if(!"ACTIVO".equals(l.getEstado()) || !"DECLARADO_APTO".equals(l.getEstadoInocuidad())
         || !ahora.isBefore(l.getLimiteReserva()) || !ahora.isBefore(l.getLimiteRecojo())
         || !ahora.isBefore(l.getFechaVencimiento())) throw invalido("Lote agotado, bloqueado o fuera del horario seguro declarado");
 }
 public Map<String,Object> vista(LoteExcedente l) {
     Producto p=l.getProducto(); Establecimiento e=l.getEstablecimiento();
     Map<String,Object> m=new LinkedHashMap<>();
     m.put("id",l.getId());m.put("nombre",p.getNombre());m.put("categoria",p.getCategoria());
     m.put("descripcion",p.getDescripcion());m.put("alergenos",p.getAlergenos());m.put("imagenUrl",p.getImagenUrl());
     m.put("modalidad",l.getModalidad());m.put("stockInicial",l.getStockInicial());
     m.put("stockDisponible",l.getStockDisponible());m.put("stockReservado",l.getStockReservado());
     m.put("precioOriginal",l.getPrecioOriginal());m.put("precioLiquidacion",l.getPrecioLiquidacion());
     m.put("pesoPorUnidadKg",l.getPesoPorUnidadKg());m.put("conservacion",l.getConservacion());
     m.put("fechaVencimiento",l.getFechaVencimiento());m.put("limiteReserva",l.getLimiteReserva());
     m.put("limiteRecojo",l.getLimiteRecojo());m.put("estado",l.getEstado());
     m.put("estadoInocuidad",l.getEstadoInocuidad());m.put("comercio",e.getNombreComercial());
     m.put("establecimientoId",e.getId());m.put("distrito",e.getDistrito());m.put("direccion",e.getDireccion());
     m.put("latitud",e.getLatitud());m.put("longitud",e.getLongitud());m.put("propietarioId",l.getPropietarioId());
     m.put("politicaAsignacion",l.getPoliticaAsignacion());
     return m;
 }
 @Transactional(readOnly=true) public List<Map<String,Object>> catalogo(){
     return lotes.findAllByOrderByIdDesc().stream().filter(l->"ACTIVO".equals(l.getEstado())&&l.getStockDisponible()>0
       && "DECLARADO_APTO".equals(l.getEstadoInocuidad())&& LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getLimiteReserva())
       && LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getLimiteRecojo())&&LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getFechaVencimiento()))
       .map(this::vista).toList();
 }
 @Transactional(readOnly=true) public List<Map<String,Object>> misLotes(String username){Usuario u=actual(username);rol(u,"VENDEDOR");return lotes.findByPropietarioIdOrderByIdDesc(u.getId()).stream().map(this::vista).toList();}
 @Transactional(readOnly=true) public Map<String,Object> detalle(Long id){return vista(lote(id));}

 @Transactional public Establecimiento registrarComercio(String username,EstablecimientoDTO d){
     Usuario u=actual(username);rol(u,"VENDEDOR");
     if(d.getNombreComercial()==null||d.getNombreComercial().isBlank()||d.getDireccion()==null||d.getDireccion().isBlank()) throw invalido("Nombre y dirección obligatorios");
     Establecimiento e=new Establecimiento();e.setUsuarioResponsableId(u.getId());
     e.setNombreComercial(d.getNombreComercial());e.setDireccion(d.getDireccion());e.setDistrito(d.getDistrito());
     e.setLatitud(d.getLatitud());e.setLongitud(d.getLongitud());e.setCorreo(d.getCorreo());e.setTelefono(d.getTelefono());
     e.setHorarioAtencion(d.getHorarioAtencion());e.setHorarioRecojo(d.getHorarioRecojo());
     e.setRuc(d.getRuc());e.setNombreResponsable(d.getNombreResponsable());e.setTelefonoResponsable(d.getTelefonoResponsable());
     e.setMinutosPreparacion(d.getMinutosPreparacion()==null?10:d.getMinutosPreparacion());e.setVende(true);e.setDona(true);
     return establecimientos.save(e);
 }
 @Transactional(readOnly=true) public List<Establecimiento> misComercios(String username){
     Usuario u=actual(username);rol(u,"VENDEDOR");return establecimientos.findAll().stream().filter(e->Objects.equals(e.getUsuarioResponsableId(),u.getId())).toList();
 }
 @Transactional public Map<String,Object> publicar(String username, PublicarLoteDTO d){
     Usuario u=actual(username);rol(u,"VENDEDOR");
     Establecimiento e=establecimientos.findById(d.getEstablecimientoId()==null?-1L:d.getEstablecimientoId()).orElseThrow(()->invalido("Registra primero tu establecimiento"));
     dueño(e.getUsuarioResponsableId(),u);
     if(d.getNombre()==null||d.getNombre().isBlank()||d.getCategoria()==null||d.getModalidad()==null
       ||d.getCantidad()==null||d.getCantidad()<1||d.getCantidad()>100000||d.getFechaVencimiento()==null
       ||d.getLimiteReserva()==null||d.getLimiteRecojo()==null) throw invalido("Faltan campos requeridos de publicación");
     if(!List.of("VENTA","DONACION").contains(d.getModalidad())) throw invalido("Modalidad inválida");
     LocalDateTime ahora=LocalDateTime.now(java.time.ZoneId.of("America/Lima"));
     if(!d.getLimiteReserva().isAfter(ahora)||!d.getLimiteRecojo().isAfter(ahora)||!d.getFechaVencimiento().isAfter(ahora)
       ||d.getLimiteReserva().isAfter(d.getLimiteRecojo())||d.getLimiteRecojo().isAfter(d.getFechaVencimiento())
       ||(d.getFechaPreparacion()!=null&&d.getFechaPreparacion().isAfter(ahora))) throw invalido("Revisa fechas: preparación <= ahora < reserva <= recojo <= vencimiento");
     if(d.getConservacion()==null||d.getConservacion().isBlank()||d.getAlergenos()==null||d.getAlergenos().isBlank()) throw invalido("Conservación y alérgenos deben declararse");
     if("VENTA".equals(d.getModalidad())&&(d.getPrecioLiquidacion()==null||d.getPrecioLiquidacion()<0)) throw invalido("Precio inválido");
     Producto p=new Producto();p.setNombre(d.getNombre());p.setCategoria(d.getCategoria());p.setDescripcion(d.getDescripcion());p.setIngredientes(d.getIngredientes());
     p.setAlergenos(d.getAlergenos());p.setImagenUrl(d.getImagenUrl());p.setPrecioOriginal(d.getPrecioOriginal());p.setPrecioDescuento("VENTA".equals(d.getModalidad())?d.getPrecioLiquidacion():0d);
     p.setPesoPorUnidadKg(d.getPesoPorUnidadKg());p.setStock(d.getCantidad());p.setEstablecimiento(e);p.setRequiereRefrigeracion("Refrigerado".equalsIgnoreCase(d.getConservacion()));
     productos.save(p);
     LoteExcedente l=new LoteExcedente();l.setProducto(p);l.setEstablecimiento(e);l.setPropietarioId(u.getId());l.setModalidad(d.getModalidad());
     l.setStockInicial(d.getCantidad());l.setStockDisponible(d.getCantidad());l.setStockReservado(0);
     l.setPrecioOriginal(d.getPrecioOriginal());l.setPrecioLiquidacion("VENTA".equals(d.getModalidad())?d.getPrecioLiquidacion():0d);l.setPesoPorUnidadKg(d.getPesoPorUnidadKg());
     l.setFechaPreparacion(d.getFechaPreparacion());l.setFechaVencimiento(d.getFechaVencimiento());l.setLimiteReserva(d.getLimiteReserva());l.setLimiteRecojo(d.getLimiteRecojo());
     l.setConservacion(d.getConservacion());l.setPoliticaAsignacion(d.getPoliticaAsignacion()==null?"MANUAL":d.getPoliticaAsignacion());
     lotes.save(l);movimiento(l,u,d.getCantidad(),"PUBLICACION_INICIAL");return vista(l);
 }
 @Transactional public Map<String,Object> pausarLote(String username,Long id,String estado){
     Usuario u=actual(username);LoteExcedente l=bloqueado(id);dueño(l.getPropietarioId(),u);
     if(!"PAUSADO".equals(estado)&&!"ACTIVO".equals(estado)) throw invalido("Solo ACTIVO o PAUSADO");
     if("ACTIVO".equals(estado)) {if(l.getStockDisponible()<1)throw invalido("Sin stock");l.setEstado("ACTIVO");valido(l);} else l.setEstado("PAUSADO");
     return vista(l);
 }
 @Transactional public OrganizacionBeneficiaria guardarOrganizacion(String username,OrganizacionDTO d){
     Usuario u=actual(username);rol(u,"ORGANIZACION");
     if(d.getRazonSocial()==null||d.getRazonSocial().isBlank()||d.getDireccion()==null||d.getDireccion().isBlank())throw invalido("Nombre y dirección requeridos");
     OrganizacionBeneficiaria o=organizaciones.findByUsuarioId(u.getId()).orElseGet(OrganizacionBeneficiaria::new);
     o.setUsuarioId(u.getId());o.setRazonSocial(d.getRazonSocial());o.setIdentificacion(d.getIdentificacion());o.setDireccion(d.getDireccion());o.setDistrito(d.getDistrito());
     o.setContacto(d.getContacto());o.setCapacidadRaciones(d.getCapacidadRaciones());o.setTransportePropio(Boolean.TRUE.equals(d.getTransportePropio()));
     o.setRefrigeracion(Boolean.TRUE.equals(d.getRefrigeracion()));o.setLatitud(d.getLatitud());o.setLongitud(d.getLongitud());
     return organizaciones.save(o);
 }
 @Transactional(readOnly=true) public Optional<OrganizacionBeneficiaria> miOrganizacion(String username){Usuario u=actual(username);rol(u,"ORGANIZACION");return organizaciones.findByUsuarioId(u.getId());}
 @Transactional public void verificarOrganizacion(String username,Long id){Usuario admin=actual(username);rol(admin,"ADMIN");OrganizacionBeneficiaria o=organizaciones.findById(id).orElseThrow(()->invalido("Organización inexistente"));o.setVerificada(true);Usuario u=usuarios.findById(o.getUsuarioId()).orElseThrow();u.setVerificado(true);avisar(u.getId(),"Cuenta verificada","Tu organización puede solicitar donaciones");}
 @Transactional(readOnly=true) public List<OrganizacionBeneficiaria> organizacionesPendientes(String username){Usuario u=actual(username);rol(u,"ADMIN");return organizaciones.findAll().stream().filter(o->!Boolean.TRUE.equals(o.getVerificada())).toList();}

 @Transactional public Solicitud pedir(String username,SolicitudFoodDTO d){
     Usuario u=actual(username);if(d.getCantidad()==null||d.getCantidad()<1||d.getLoteId()==null)throw invalido("Cantidad e ID de lote requeridos");
     LoteExcedente l=bloqueado(d.getLoteId());valido(l);
     if(Objects.equals(u.getId(),l.getPropietarioId())) throw invalido("No puedes solicitar tu propio lote");
     if(d.getCantidad()>l.getStockDisponible()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Stock insuficiente. Recarga el catálogo");
     if("DONACION".equals(l.getModalidad())){
         rol(u,"ORGANIZACION");OrganizacionBeneficiaria o=organizaciones.findByUsuarioId(u.getId()).orElseThrow(()->invalido("Registra una organización antes de solicitar"));
         if(!Boolean.TRUE.equals(o.getVerificada()))throw invalido("Organización pendiente de verificación");
         if(o.getCapacidadRaciones()!=null && d.getCantidad()>o.getCapacidadRaciones())throw invalido("Cantidad superior a capacidad declarada");
         if("Refrigerado".equalsIgnoreCase(l.getConservacion())&&!Boolean.TRUE.equals(o.getRefrigeracion()))throw invalido("Para este lote se requiere refrigeración");
     } else rol(u,"COMPRADOR");
     Solicitud s=new Solicitud();s.setLoteId(l.getId());s.setSolicitanteId(u.getId());s.setPropietarioId(l.getPropietarioId());s.setTipo(l.getModalidad());
     s.setCantidadSolicitada(d.getCantidad());s.setComentario(d.getComentario());
     if("VENTA".equals(l.getModalidad())){
         l.setStockDisponible(l.getStockDisponible()-d.getCantidad());l.setStockReservado(l.getStockReservado()+d.getCantidad());
         l.getProducto().setStock(l.getStockDisponible());
         s.setCantidadAsignada(d.getCantidad());s.setEstado("ASIGNADA");s.setCodigoRetiro(codigo());s.setConfirmadaEn(LocalDateTime.now(java.time.ZoneId.of("America/Lima")));
         movimiento(l,u,-d.getCantidad(),"RESERVA_VENTA");
     } else s.setEstado("PENDIENTE");
     solicitudes.save(s);
     if("ASIGNADA".equals(s.getEstado())) crearEntrega(s,l);
     avisar(l.getPropietarioId(),"Nueva solicitud","Recibiste solicitud #"+s.getId()+" del lote "+l.getId());
     avisar(u.getId(),"Solicitud registrada","Estado: "+s.getEstado()+". ID #"+s.getId());
     return s;
 }
 private void crearEntrega(Solicitud s,LoteExcedente l){
     if(entregas.findBySolicitudId(s.getId()).isPresent())return;
     Entrega e=new Entrega();e.setSolicitudId(s.getId());e.setOrigen(l.getEstablecimiento().getDireccion());e.setModalidad("RECOJO");
     e.setHoraEstimada(l.getLimiteRecojo());entregas.save(e);
 }
 @Transactional public Solicitud asignar(String username,Long id,int cantidad){
     Usuario u=actual(username);Solicitud s=solicitud(id);dueño(s.getPropietarioId(),u);
     if(!"DONACION".equals(s.getTipo())||!"PENDIENTE".equals(s.getEstado()))throw invalido("Solicitud no pendiente");
     LoteExcedente l=bloqueado(s.getLoteId());valido(l);
     if(cantidad<1||cantidad>s.getCantidadSolicitada()||cantidad>l.getStockDisponible())throw new ResponseStatusException(HttpStatus.CONFLICT,"Cantidad superior a stock disponible o a lo solicitado");
     l.setStockDisponible(l.getStockDisponible()-cantidad);l.setStockReservado(l.getStockReservado()+cantidad);
     l.getProducto().setStock(l.getStockDisponible());
     s.setCantidadAsignada(cantidad);
     s.setEstado(cantidad<s.getCantidadSolicitada()?"PARCIAL_OFRECIDA":"ASIGNADA");
     if("ASIGNADA".equals(s.getEstado())){s.setCodigoRetiro(codigo());s.setConfirmadaEn(LocalDateTime.now(java.time.ZoneId.of("America/Lima")));crearEntrega(s,l);}
     movimiento(l,u,-cantidad,"ASIGNACION_DONACION");
     avisar(s.getSolicitanteId(),"Donación: decisión","Solicitud #"+s.getId()+" "+s.getEstado()+" por "+cantidad+" unidad(es)");
     return s;
 }
 @Transactional public Solicitud aceptarParcial(String username,Long id, boolean aceptar){
     Usuario u=actual(username);Solicitud s=solicitud(id);if(!Objects.equals(s.getSolicitanteId(),u.getId()))throw new AccessDeniedException("No es tu solicitud");
     if(!"PARCIAL_OFRECIDA".equals(s.getEstado()))throw invalido("No hay propuesta parcial");
     LoteExcedente l=bloqueado(s.getLoteId());
     if(aceptar){valido(l);s.setEstado("ASIGNADA");s.setCodigoRetiro(codigo());s.setConfirmadaEn(LocalDateTime.now(java.time.ZoneId.of("America/Lima")));crearEntrega(s,l);}
     else {liberar(s,l,u);s.setEstado("RECHAZADA");}
     avisar(s.getPropietarioId(),"Respuesta a propuesta parcial","Solicitud #"+id+" "+s.getEstado());return s;
 }
 private void liberar(Solicitud s,LoteExcedente l,Usuario u){
     int cantidad=s.getCantidadAsignada()==null?0:s.getCantidadAsignada();
     l.setStockReservado(l.getStockReservado()-cantidad);
     if("ACTIVO".equals(l.getEstado())&&LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getLimiteReserva())&&LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getFechaVencimiento()))
        l.setStockDisponible(l.getStockDisponible()+cantidad);
     l.getProducto().setStock(l.getStockDisponible());s.setCantidadAsignada(0);movimiento(l,u,cantidad,"LIBERACION_RESERVA");
 }
 @Transactional public Solicitud cancelar(String username,Long id){
     Usuario u=actual(username);Solicitud s=solicitud(id);
     if(!Objects.equals(s.getSolicitanteId(),u.getId())&&!Objects.equals(s.getPropietarioId(),u.getId())&&!"ADMIN".equals(u.getRol()))throw new AccessDeniedException("No eres parte de esta solicitud");
     if(List.of("ENTREGADA","CANCELADA","RECHAZADA","LISTA").contains(s.getEstado()))throw invalido("No es posible cancelar el estado actual");
     LoteExcedente l=bloqueado(s.getLoteId());liberar(s,l,u);s.setEstado("CANCELADA");
     entregas.findBySolicitudId(s.getId()).ifPresent(e->e.setEstado("CANCELADA"));
     avisar(s.getSolicitanteId(),"Solicitud cancelada","Solicitud #"+id);avisar(s.getPropietarioId(),"Solicitud cancelada","Solicitud #"+id);return s;
 }
 @Transactional public Solicitud marcarLista(String username,Long id){
     Usuario u=actual(username);Solicitud s=solicitud(id);dueño(s.getPropietarioId(),u);
     if(!"ASIGNADA".equals(s.getEstado()))throw invalido("Debe estar asignada");
     LoteExcedente l=bloqueado(s.getLoteId());if(!LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getLimiteRecojo()))throw invalido("Ventana de recojo terminada");
     s.setEstado("LISTA");entregas.findBySolicitudId(id).ifPresent(e->{e.setEstado("LISTA");e.setListaEn(LocalDateTime.now(java.time.ZoneId.of("America/Lima")));});
     avisar(s.getSolicitanteId(),"Listo para recojo","Solicitud #"+id);return s;
 }
 @Transactional public Solicitud entregar(String username,Long id,String codigo){
     Usuario u=actual(username);Solicitud s=solicitud(id);dueño(s.getPropietarioId(),u);
     if(!"LISTA".equals(s.getEstado()) || codigo==null || !codigo.equals(s.getCodigoRetiro()))throw invalido("Solicitud no lista o código incorrecto");
     LoteExcedente l=bloqueado(s.getLoteId());
     if(!LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getLimiteRecojo())||!LocalDateTime.now(java.time.ZoneId.of("America/Lima")).isBefore(l.getFechaVencimiento()))throw invalido("La ventana de entrega ya terminó");
     s.setEstado("ENTREGADA");s.setEntregadaEn(LocalDateTime.now(java.time.ZoneId.of("America/Lima")));
     l.setStockReservado(l.getStockReservado()-s.getCantidadAsignada());
     entregas.findBySolicitudId(id).ifPresent(e->{e.setEstado("ENTREGADA");e.setEntregadaEn(LocalDateTime.now(java.time.ZoneId.of("America/Lima")));});
     movimiento(l,u,0,"ENTREGA_CONFIRMADA");avisar(s.getSolicitanteId(),"Entrega confirmada","Solicitud #"+id);return s;
 }
 @Transactional(readOnly=true) public List<Solicitud> misSolicitudes(String username){return solicitudes.findBySolicitanteIdOrderByIdDesc(actual(username).getId());}
 @Transactional(readOnly=true) public List<Solicitud> solicitudesComercio(String username){Usuario u=actual(username);rol(u,"VENDEDOR");return solicitudes.findByPropietarioIdOrderByIdDesc(u.getId());}
 @Transactional(readOnly=true) public Entrega entrega(String username,Long id){Usuario u=actual(username);Solicitud s=solicitud(id);if(!"ADMIN".equals(u.getRol())&&!Objects.equals(u.getId(),s.getSolicitanteId())&&!Objects.equals(u.getId(),s.getPropietarioId()))throw new AccessDeniedException("No corresponde a tu entrega");return entregas.findBySolicitudId(id).orElseThrow(()->invalido("Sin entrega asignada"));}
 @Transactional(readOnly=true) public List<Notificacion> misAvisos(String username){return notificaciones.findByUsuarioIdOrderByIdDesc(actual(username).getId());}
 @Transactional public Incidencia incidencia(String username,IncidenciaDTO d){Usuario u=actual(username);if(d.getDescripcion()==null||d.getDescripcion().isBlank())throw invalido("Describe el problema");if(d.getSolicitudId()!=null){Solicitud s=solicitud(d.getSolicitudId());if(!Objects.equals(u.getId(),s.getPropietarioId())&&!Objects.equals(u.getId(),s.getSolicitanteId())&&!"ADMIN".equals(u.getRol()))throw new AccessDeniedException("No corresponde a tu operación");}
     Incidencia i=new Incidencia();i.setUsuarioId(u.getId());i.setSolicitudId(d.getSolicitudId());i.setTipo(d.getTipo());i.setDescripcion(d.getDescripcion());return incidencias.save(i);}
 @Transactional(readOnly=true) public List<Incidencia> todasIncidencias(String username){Usuario u=actual(username);rol(u,"ADMIN");return incidencias.findAll();}
 @Transactional(readOnly=true) public List<Incidencia> misIncidencias(String username){return incidencias.findByUsuarioIdOrderByIdDesc(actual(username).getId());}
 @Transactional public Incidencia resolverIncidencia(String username,Long id,String texto){Usuario u=actual(username);rol(u,"ADMIN");Incidencia i=incidencias.findById(id).orElseThrow(()->invalido("Incidencia inexistente"));i.setEstado("RESUELTA");i.setResolucion(texto);avisar(i.getUsuarioId(),"Incidencia resuelta","Incidencia #"+id);return i;}
 @Transactional public HistorialConservacion registrarConservacion(String username,Long id,Double temperatura,String obs){Usuario u=actual(username);LoteExcedente l=lote(id);dueño(l.getPropietarioId(),u);HistorialConservacion h=new HistorialConservacion();h.setLoteId(id);h.setResponsableId(u.getId());h.setTemperaturaCelsius(temperatura);h.setObservaciones(obs);return conservaciones.save(h);}
 @Transactional(readOnly=true) public List<HistorialConservacion> historialConservacion(String username,Long id){Usuario u=actual(username);LoteExcedente l=lote(id);dueño(l.getPropietarioId(),u);return conservaciones.findByLoteIdOrderByFechaDesc(id);}
 @Transactional(readOnly=true) public Map<String,Object> metricas(String username){
     Usuario u=actual(username);rol(u,"VENDEDOR");List<Solicitud> ss=solicitudes.findByPropietarioIdOrderByIdDesc(u.getId());
     long entregadas=ss.stream().filter(s->"ENTREGADA".equals(s.getEstado())).count();
     int unidades=ss.stream().filter(s->"ENTREGADA".equals(s.getEstado())).mapToInt(s->s.getCantidadAsignada()==null?0:s.getCantidadAsignada()).sum();
     double ingreso=ss.stream().filter(s->"ENTREGADA".equals(s.getEstado()) && "VENTA".equals(s.getTipo())).mapToDouble(s->{LoteExcedente l=lote(s.getLoteId());return s.getCantidadAsignada()*(l.getPrecioLiquidacion()==null?0:l.getPrecioLiquidacion());}).sum();
     Map<String,Object> m=new LinkedHashMap<>();m.put("entregasConfirmadas",entregadas);m.put("unidadesEntregadas",unidades);m.put("ingresosRecuperados",Math.round(ingreso*100)/100d);
     m.put("lotesPublicados",lotes.findByPropietarioIdOrderByIdDesc(u.getId()).size());m.put("solicitudes",ss.size());return m;
 }
 @Transactional(readOnly=true) public Map<String,Object> estimacion(String username){
     Usuario u=actual(username);rol(u,"VENDEDOR");List<Solicitud> ss=solicitudes.findByPropietarioIdOrderByIdDesc(u.getId()).stream().filter(s->"VENTA".equals(s.getTipo())&&"ENTREGADA".equals(s.getEstado())).toList();
     if(ss.size()<5)return Map.of("disponible",false,"mensaje","Se requieren al menos cinco ventas entregadas para estimar; sin inventar predicciones");
     double avg=ss.stream().mapToInt(s->s.getCantidadAsignada()==null?0:s.getCantidadAsignada()).average().orElse(0);
     return Map.of("disponible",true,"tipo","estimacion_estadistica_simple_no_IA_entrenada","promedioUnidadesPorVenta",Math.round(avg*100)/100d,"muestra",ss.size());
 }

    // Metodos escritos de forma explicita, sin Lombok.
    public FoodLoopService(UsuarioRepository usuarios, EstablecimientoRepository establecimientos, ProductoRepository productos, LoteExcedenteRepository lotes, SolicitudRepository solicitudes, EntregaRepository entregas, NotificacionRepository notificaciones, MovimientoInventarioRepository movimientos, HistorialConservacionRepository conservaciones, OrganizacionBeneficiariaRepository organizaciones, IncidenciaRepository incidencias) {
        this.usuarios = usuarios;
        this.establecimientos = establecimientos;
        this.productos = productos;
        this.lotes = lotes;
        this.solicitudes = solicitudes;
        this.entregas = entregas;
        this.notificaciones = notificaciones;
        this.movimientos = movimientos;
        this.conservaciones = conservaciones;
        this.organizaciones = organizaciones;
        this.incidencias = incidencias;
    }

}
