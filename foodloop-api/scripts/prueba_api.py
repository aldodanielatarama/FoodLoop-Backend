"""Pruebas HTTP de FoodLoop. Ejecutar SOLO con el perfil demo.
Requiere que el backend ya esté iniciado y que la BD sea de pruebas.
No necesita dependencias Python externas.
"""
import datetime, json, os, urllib.request, urllib.error
from zoneinfo import ZoneInfo

BASE=os.environ.get('FOODLOOP_URL','http://localhost:8080')
PASSWORD=os.environ.get('FOODLOOP_DEMO_PASSWORD','Demo1234!')

def call(path, payload=None, token=None, method='GET'):
    data=json.dumps(payload).encode() if payload is not None else None
    h={'Content-Type':'application/json'}
    if token:h['Authorization']='Bearer '+token
    req=urllib.request.Request(BASE+path, data=data, headers=h, method='POST' if data is not None and method=='GET' else method)
    try:
        with urllib.request.urlopen(req,timeout=12) as r:
            body=r.read().decode();return r.status,json.loads(body) if body and body[0] in '[{"' else body
    except urllib.error.HTTPError as e:
        body=e.read().decode();return e.code,body

def check(condition,message):
    print(('OK ' if condition else 'FALLÓ ')+message)
    if not condition:raise AssertionError(message)

def signin(username):
    status, data=call('/auth/session',{'username':username,'password':PASSWORD})
    check(status==200,f'login {username}');return data['token']

if __name__=='__main__':
    print('ESTAS PRUEBAS CREAN LOTES Y RESERVAS. USA UNA BD DE PRUEBAS.')
    seller=signin('comercio_demo');buyer=signin('comprador_demo');org=signin('ong_demo')
    status,shops=call('/api/foodloop/mi/comercios',token=seller)
    check(status==200 and shops,'Hay un establecimiento del vendedor')
    status,catalog=call('/api/foodloop/lotes')
    check(status==200 and len(catalog)>0,'Catálogo público disponible')
    now=datetime.datetime.now(ZoneInfo('America/Lima'))
    def after(hours):return (now+datetime.timedelta(hours=hours)).strftime('%Y-%m-%dT%H:%M:%S')
    oferta={
      'establecimientoId':shops[0]['id'],'nombre':'Prueba automatizada - pan del día',
      'categoria':'Panadería','modalidad':'VENTA','cantidad':1,'precioLiquidacion':2.0,'precioOriginal':5.0,
      'conservacion':'Ambiente protegido','alergenos':'Gluten','fechaVencimiento':after(6),
      'limiteRecojo':after(4),'limiteReserva':after(3)
    }
    status,lote=call('/api/foodloop/lotes',oferta,seller)
    check(status==200 and lote['stockDisponible']==1,'Publicar nuevo lote real')
    status,sol=call('/api/foodloop/solicitudes',{'loteId':lote['id'],'cantidad':1},buyer)
    check(status==201 and sol['estado']=='ASIGNADA','Reserva exitosa')
    status,other=call('/api/foodloop/solicitudes',{'loteId':lote['id'],'cantidad':1},buyer)
    check(status==409,'No vender doble stock')
    status,step=call(f"/api/foodloop/solicitudes/{sol['id']}/lista",token=seller,method='POST')
    check(status==200 and step['estado']=='LISTA','Marcar listo')
    status,step=call(f"/api/foodloop/solicitudes/{sol['id']}/entregar?codigo={sol['codigoRetiro']}",token=seller,method='POST')
    check(status==200 and step['estado']=='ENTREGADA','Entrega validada por código')
    status,_=call('/api/foodloop/admin/organizaciones',token=buyer)
    check(status==403,'No acceder como comprador a administración')
    status,donations=call('/api/foodloop/lotes')
    found=next((l for l in donations if l['modalidad']=='DONACION'),None)
    check(found is not None,'Hay donaciones de prueba')
    status,dreq=call('/api/foodloop/solicitudes',{'loteId':found['id'],'cantidad':2},org)
    check(status==201 and dreq['estado']=='PENDIENTE','ONG solicita donación sin bloquear inventario')
    status,assigned=call(f"/api/foodloop/solicitudes/{dreq['id']}/asignar?cantidad=1",token=seller,method='POST')
    check(status==200 and assigned['estado']=='PARCIAL_OFRECIDA','Vendedor asigna parcial')
    status,accepted=call(f"/api/foodloop/solicitudes/{dreq['id']}/aceptar-parcial?aceptar=true",token=org,method='POST')
    check(status==200 and accepted['estado']=='ASIGNADA','Organización acepta parcial')
    print('PRUEBAS FUNCIONALES COMPLETADAS')
