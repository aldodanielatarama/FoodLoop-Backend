import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Lote, LoteFormulario, Establecimiento, EstablecimientoFormulario,
  Solicitud, Organizacion, Entrega, Notificacion, Incidencia,
  Metrica, Estimacion, Conservacion
} from '../models/foodloop.models';

@Injectable({ providedIn: 'root' })
export class FoodloopService {
  private readonly base = '/api/foodloop';
  constructor(private http: HttpClient) {}

  catalogo(): Observable<Lote[]> { return this.http.get<Lote[]>(this.base + '/lotes'); }
  detalle(id: number): Observable<Lote> { return this.http.get<Lote>(`${this.base}/lotes/${id}`); }
  misComercios(): Observable<Establecimiento[]> { return this.http.get<Establecimiento[]>(this.base + '/mi/comercios'); }
  crearComercio(datos: EstablecimientoFormulario): Observable<Establecimiento> {
    return this.http.post<Establecimiento>(this.base + '/comercios', datos);
  }
  misLotes(): Observable<Lote[]> { return this.http.get<Lote[]>(this.base + '/mi/lotes'); }
  publicar(datos: LoteFormulario): Observable<Lote> { return this.http.post<Lote>(this.base + '/lotes', datos); }
  cambiarEstado(id: number, valor: 'ACTIVO' | 'PAUSADO'): Observable<Lote> {
    return this.http.patch<Lote>(`${this.base}/lotes/${id}/estado`, null, { params: { valor } });
  }
  perfilOrganizacion(): Observable<Organizacion | null> {
    return this.http.get<Organizacion | null>(this.base + '/organizaciones/mi-perfil');
  }
  guardarOrganizacion(datos: Organizacion): Observable<Organizacion> {
    return this.http.post<Organizacion>(this.base + '/organizaciones/perfil', datos);
  }
  pendientesOrganizaciones(): Observable<Organizacion[]> {
    return this.http.get<Organizacion[]>(this.base + '/admin/organizaciones');
  }
  verificarOrganizacion(id: number): Observable<{estado:string}> {
    return this.http.post<{estado:string}>(`${this.base}/admin/organizaciones/${id}/verificar`, {});
  }
  solicitar(loteId: number, cantidad: number, comentario: string): Observable<Solicitud> {
    return this.http.post<Solicitud>(this.base + '/solicitudes', { loteId, cantidad, comentario });
  }
  misSolicitudes(): Observable<Solicitud[]> { return this.http.get<Solicitud[]>(this.base + '/mi/solicitudes'); }
  solicitudesComercio(): Observable<Solicitud[]> { return this.http.get<Solicitud[]>(this.base + '/comercio/solicitudes'); }
  asignar(id: number, cantidad: number): Observable<Solicitud> {
    return this.http.post<Solicitud>(`${this.base}/solicitudes/${id}/asignar`, {}, { params: { cantidad } });
  }
  aceptarParcial(id: number, aceptar: boolean): Observable<Solicitud> {
    return this.http.post<Solicitud>(`${this.base}/solicitudes/${id}/aceptar-parcial`, {}, { params: { aceptar } });
  }
  cancelar(id: number): Observable<Solicitud> {
    return this.http.post<Solicitud>(`${this.base}/solicitudes/${id}/cancelar`, {});
  }
  marcarLista(id: number): Observable<Solicitud> {
    return this.http.post<Solicitud>(`${this.base}/solicitudes/${id}/lista`, {});
  }
  confirmarEntrega(id: number, codigo: string): Observable<Solicitud> {
    return this.http.post<Solicitud>(`${this.base}/solicitudes/${id}/entregar`, {}, { params: { codigo } });
  }
  entrega(id: number): Observable<Entrega> { return this.http.get<Entrega>(`${this.base}/solicitudes/${id}/entrega`); }
  notificaciones(): Observable<Notificacion[]> { return this.http.get<Notificacion[]>(this.base + '/mi/notificaciones'); }
  crearIncidencia(solicitudId: number | null, tipo: string, descripcion: string): Observable<Incidencia> {
    return this.http.post<Incidencia>(this.base + '/incidencias', {solicitudId, tipo, descripcion});
  }
  misIncidencias(): Observable<Incidencia[]> { return this.http.get<Incidencia[]>(this.base + '/mi/incidencias'); }
  todasIncidencias(): Observable<Incidencia[]> { return this.http.get<Incidencia[]>(this.base + '/admin/incidencias'); }
  resolverIncidencia(id: number, nota: string): Observable<Incidencia> {
    return this.http.post<Incidencia>(`${this.base}/admin/incidencias/${id}/resolver`, {}, { params: { nota } });
  }
  registrarConservacion(loteId: number, temperatura: number, observacion: string): Observable<Conservacion> {
    let params = new HttpParams().set('observacion', observacion);
    if (Number.isFinite(temperatura)) params = params.set('temperatura', temperatura);
    return this.http.post<Conservacion>(`${this.base}/lotes/${loteId}/conservacion`, {}, { params });
  }
  historialConservacion(loteId: number): Observable<Conservacion[]> {
    return this.http.get<Conservacion[]>(`${this.base}/lotes/${loteId}/conservacion`);
  }
  metricas(): Observable<Metrica> { return this.http.get<Metrica>(this.base + '/comercio/metricas'); }
  estimacion(): Observable<Estimacion> { return this.http.get<Estimacion>(this.base + '/comercio/estimacion'); }
  estimarRuta(origenLat: number, origenLon: number, destinoLat: number, destinoLon: number): Observable<Record<string,unknown>> {
    return this.http.get<Record<string,unknown>>(this.base + '/rutas/eta', {
      params: { origenLat, origenLon, destinoLat, destinoLon }
    });
  }
}
