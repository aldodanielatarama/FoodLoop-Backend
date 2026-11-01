import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FoodloopService } from '../services/foodloop.service';
import { Organizacion, Incidencia } from '../models/foodloop.models';

@Component({selector:'app-administracion',standalone:true,imports:[CommonModule,FormsModule],templateUrl:'./administracion.component.html'})
export class AdministracionComponent implements OnInit {
  organizaciones:Organizacion[]=[]; incidencias:Incidencia[]=[]; notas:Record<number,string>={};
  mensaje='';error='';
  constructor(private api:FoodloopService){}
  ngOnInit():void{this.cargar();}
  cargar():void {
    this.api.pendientesOrganizaciones().subscribe({next:o=>this.organizaciones=o,error:()=>this.error='No se pudieron consultar las organizaciones.'});
    this.api.todasIncidencias().subscribe({next:i=>this.incidencias=i,error:()=>this.error='No se pudieron consultar las incidencias.'});
  }
  verificar(id:number|undefined):void {
    if(id===undefined)return;
    this.api.verificarOrganizacion(id).subscribe({next:()=>{this.mensaje='Organización verificada.';this.cargar();},error:()=>this.error='No se pudo verificar.'});
  }
  resolver(id:number):void {
    const nota=this.notas[id];if(!nota?.trim()){this.error='Escribe una resolución.';return;}
    this.api.resolverIncidencia(id,nota).subscribe({next:()=>{this.mensaje='Incidencia resuelta.';this.cargar();},error:()=>this.error='No se pudo resolver.'});
  }
}
