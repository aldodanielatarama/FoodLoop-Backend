import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { FoodloopService } from '../services/foodloop.service';
import { Incidencia, Notificacion, PerfilUsuario, Solicitud, Entrega } from '../models/foodloop.models';
import { VendedorComponent } from './vendedor.component';
import { OrganizacionComponent } from './organizacion.component';
import { AdministracionComponent } from './administracion.component';

@Component({
  selector: 'app-panel', standalone:true,
  imports:[CommonModule,FormsModule,RouterLink,VendedorComponent,OrganizacionComponent,AdministracionComponent],
  templateUrl:'./panel.component.html'
})
export class PanelComponent implements OnInit {
  perfil:PerfilUsuario|null=null;
  solicitudes:Solicitud[]=[]; avisos:Notificacion[]=[]; incidencias:Incidencia[]=[];
  entrega:Entrega|null=null;
  ruta = { origenLat: 0, origenLon: 0, destinoLat: 0, destinoLon: 0 };
  resultadoRuta: Record<string, unknown> | null = null;
  incidencia={solicitudId:null as number|null, tipo:'RECOJO', descripcion:''};
  error='';mensaje='';
  constructor(public auth:AuthService,private api:FoodloopService,private router:Router){}
  ngOnInit():void {
    if(!this.auth.sesion){this.router.navigateByUrl('/acceso');return;}
    this.auth.miPerfil().subscribe({next:p=>this.perfil=p,error:()=>{this.auth.salir();this.router.navigateByUrl('/acceso');}});
    this.actualizar();
  }
  actualizar():void {
    this.api.misSolicitudes().subscribe({next:s=>this.solicitudes=s,error:()=>{}});
    this.api.notificaciones().subscribe({next:n=>this.avisos=n,error:()=>{}});
    this.api.misIncidencias().subscribe({next:i=>this.incidencias=i,error:()=>{}});
  }
  cancelar(id:number):void {this.api.cancelar(id).subscribe({next:()=>{this.mensaje='Solicitud cancelada.';this.actualizar();},error:e=>this.fallo(e)});}
  consultarEntrega(id:number):void {this.api.entrega(id).subscribe({next:e=>this.entrega=e,error:e=>this.fallo(e)});}
  reportar():void {
    if(!this.incidencia.descripcion.trim()){this.error='Escribe una descripción.';return;}
    this.api.crearIncidencia(this.incidencia.solicitudId,this.incidencia.tipo,this.incidencia.descripcion).subscribe({next:()=>{this.mensaje='Incidencia registrada.';this.incidencia.descripcion='';this.actualizar();},error:e=>this.fallo(e)});
  }
  calcularRuta():void {
    this.error='';this.resultadoRuta=null;
    this.api.estimarRuta(this.ruta.origenLat,this.ruta.origenLon,this.ruta.destinoLat,this.ruta.destinoLon).subscribe({
      next:r=>this.resultadoRuta=r, error:e=>this.fallo(e)
    });
  }
  fallo(e:{error?:{detail?:string;message?:string}}):void {this.error=e.error?.detail||e.error?.message||'No se completó la operación.';}
}
