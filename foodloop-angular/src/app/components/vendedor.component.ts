import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FoodloopService } from '../services/foodloop.service';
import { Conservacion, Entrega, Establecimiento, EstablecimientoFormulario, Estimacion, Lote, LoteFormulario, Metrica, Solicitud } from '../models/foodloop.models';

@Component({ selector: 'app-vendedor', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './vendedor.component.html' })
export class VendedorComponent implements OnInit {
  comercios: Establecimiento[] = []; lotes: Lote[] = []; solicitudes: Solicitud[] = [];
  metricas: Metrica | null = null; estimacion: Estimacion | null = null;
  entregaActual: Entrega | null = null; historial: Conservacion[] = [];
  error = ''; mensaje = ''; guardando = false;
  accionCantidades: Record<number, number> = {};
  codigos: Record<number, string> = {};
  temperaturas: Record<number, number> = {};
  observaciones: Record<number, string> = {};
  comercio: EstablecimientoFormulario = {
    nombreComercial: '', direccion: '', distrito: '', telefono: '', correo: '',
    horarioAtencion: '', horarioRecojo: '', ruc: '', nombreResponsable: '', telefonoResponsable: '', minutosPreparacion: 10
  };
  lote: LoteFormulario = {
    establecimientoId: 0, nombre: '', categoria: 'Panadería', descripcion: '', ingredientes: '',
    alergenos: '', imagenUrl: '', modalidad: 'VENTA', cantidad: 1,
    precioOriginal: 0, precioLiquidacion: 0, pesoPorUnidadKg: 0.25,
    fechaPreparacion: null, fechaVencimiento: '', limiteReserva: '', limiteRecojo: '',
    conservacion: 'Ambiente protegido', politicaAsignacion: 'MANUAL'
  };
  constructor(private api: FoodloopService) {}
  ngOnInit(): void { this.cargar(); }
  cargar(): void {
    this.api.misComercios().subscribe({next: datos => { this.comercios = datos; if (!this.lote.establecimientoId && datos.length) this.lote.establecimientoId = datos[0].id; },error: () => this.error='No se pudieron cargar los comercios.'});
    this.api.misLotes().subscribe({next: datos => this.lotes=datos,error: () => this.error='No se pudieron cargar los lotes.'});
    this.api.solicitudesComercio().subscribe({next: datos => this.solicitudes=datos,error: () => this.error='No se pudieron cargar las solicitudes.'});
    this.api.metricas().subscribe({next: datos => this.metricas=datos,error: () => {}});
    this.api.estimacion().subscribe({next: datos => this.estimacion=datos,error: () => {}});
  }
  registrarComercio(): void {
    this.limpiar(); this.api.crearComercio(this.comercio).subscribe({
      next: () => {this.mensaje='Comercio registrado.'; this.cargar();},
      error: error => this.fallo(error)
    });
  }
  publicar(): void {
    this.limpiar();
    if (!(this.lote.establecimientoId && this.lote.nombre && this.lote.alergenos && this.lote.conservacion)) {this.error='Completa establecimiento, nombre, alérgenos y conservación.';return;}
    this.api.publicar(this.lote).subscribe({
      next: creado => {this.mensaje='Lote #'+creado.id+' publicado.'; this.cargar();},
      error: error => this.fallo(error)
    });
  }
  estado(id: number, valor: 'ACTIVO' | 'PAUSADO'): void {
    this.limpiar(); this.api.cambiarEstado(id,valor).subscribe({ next: () => {this.mensaje='Estado actualizado.';this.cargar();}, error: e=>this.fallo(e) });
  }
  asignar(s: Solicitud): void {
    const cantidad=this.accionCantidades[s.id] ?? s.cantidadSolicitada;
    if (!Number.isInteger(cantidad) || cantidad<1) {this.error='Cantidad de asignación inválida.';return;}
    this.limpiar(); this.api.asignar(s.id,cantidad).subscribe({next: () => {this.mensaje='Asignación realizada.';this.cargar();},error:e=>this.fallo(e)});
  }
  lista(id: number): void {this.limpiar();this.api.marcarLista(id).subscribe({next:()=>{this.mensaje='Pedido listo para recojo.';this.cargar();},error:e=>this.fallo(e)});}
  entregar(id: number): void {this.limpiar();this.api.confirmarEntrega(id,this.codigos[id]||'').subscribe({next:()=>{this.mensaje='Entrega verificada.';this.cargar();},error:e=>this.fallo(e)});}
  consultarEntrega(id: number): void {this.limpiar();this.api.entrega(id).subscribe({next: d=>this.entregaActual=d,error:e=>this.fallo(e)});}
  conservacion(id: number): void {
    this.limpiar(); this.api.registrarConservacion(id,this.temperaturas[id],this.observaciones[id]||'Control registrado').subscribe({next:()=>{this.mensaje='Registro de conservación agregado.';this.verHistorial(id);},error:e=>this.fallo(e)});
  }
  verHistorial(id: number): void {this.api.historialConservacion(id).subscribe({next: h=>this.historial=h,error:e=>this.fallo(e)});}
  limpiar(): void {this.error='';this.mensaje='';}
  fallo(error: {error?:{detail?:string;message?:string}}): void {this.error=error.error?.detail||error.error?.message||'La operación no pudo completarse. Comprueba los campos, el horario y los permisos.';}
}
