import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FoodloopService } from '../services/foodloop.service';
import { Organizacion, Solicitud, Entrega } from '../models/foodloop.models';

@Component({ selector:'app-organizacion', standalone:true, imports:[CommonModule,FormsModule], templateUrl:'./organizacion.component.html' })
export class OrganizacionComponent implements OnInit {
  perfil: Organizacion = { razonSocial:'', identificacion:'', direccion:'', distrito:'', contacto:'', capacidadRaciones:20, transportePropio:false, refrigeracion:false };
  solicitudes:Solicitud[]=[]; entrega:Entrega|null=null;
  error=''; mensaje='';
  constructor(private api:FoodloopService) {}
  ngOnInit():void { this.cargar(); }
  cargar():void {
    this.api.perfilOrganizacion().subscribe({next:p=>{if(p)this.perfil=p;},error:()=>this.error='Error al consultar perfil.'});
    this.api.misSolicitudes().subscribe({next:ss=>this.solicitudes=ss,error:()=>this.error='Error al consultar solicitudes.'});
  }
  guardar():void {
    this.error='';this.mensaje='';this.api.guardarOrganizacion(this.perfil).subscribe({next:p=>{this.perfil=p;this.mensaje='Perfil guardado. La administración debe verificarlo para solicitar donaciones.';},error:e=>this.fallo(e)});
  }
  aceptar(id:number, aceptado:boolean):void {
    this.error='';this.api.aceptarParcial(id,aceptado).subscribe({next:()=>{this.mensaje='Respuesta registrada.';this.cargar();},error:e=>this.fallo(e)});
  }
  cancelar(id:number):void {
    this.error='';this.api.cancelar(id).subscribe({next:()=>{this.mensaje='Solicitud cancelada.';this.cargar();},error:e=>this.fallo(e)});
  }
  verEntrega(id:number):void {this.api.entrega(id).subscribe({next:r=>this.entrega=r,error:e=>this.fallo(e)});}
  fallo(e:{error?:{detail?:string;message?:string}}):void {this.error=e.error?.detail||e.error?.message||'No se pudo completar la operación.';}
}
