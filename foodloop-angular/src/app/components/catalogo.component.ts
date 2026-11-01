import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { FoodloopService } from '../services/foodloop.service';
import { AuthService } from '../services/auth.service';
import { Lote, Modalidad } from '../models/foodloop.models';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MatButtonModule, MatCardModule],
  templateUrl: './catalogo.component.html'
})
export class CatalogoComponent implements OnInit {
  lotes: Lote[] = [];
  termino = '';
  modalidad: 'TODOS' | Modalidad = 'TODOS';
  seleccionado: Lote | null = null;
  cantidad = 1;
  comentario = '';
  error = '';
  exito = '';
  cargando = false;

  constructor(private api: FoodloopService, public auth: AuthService) {}
  ngOnInit(): void { this.cargar(); }
  cargar(): void {
    this.error = '';
    this.cargando = true;
    this.api.catalogo().subscribe({
      next: lotes => { this.lotes = lotes; this.cargando = false; },
      error: () => { this.error = 'No se pudo cargar el catálogo. Comprueba que Spring Boot esté iniciado.'; this.cargando = false; }
    });
  }
  get filtrados(): Lote[] {
    return this.lotes.filter(l => (this.modalidad === 'TODOS' || l.modalidad === this.modalidad)
      && (l.nombre + ' ' + l.comercio + ' ' + l.distrito + ' ' + l.categoria).toLowerCase().includes(this.termino.toLowerCase()));
  }
  abrir(lote: Lote): void { this.seleccionado = lote; this.cantidad = 1; this.comentario = ''; this.error = ''; this.exito = ''; }
  cerrar(): void { this.seleccionado = null; }
  solicitar(): void {
    if (!this.seleccionado || !Number.isInteger(this.cantidad) || this.cantidad < 1 || this.cantidad > this.seleccionado.stockDisponible) {
      this.error = 'Indica una cantidad válida según el stock disponible.'; return;
    }
    this.error = ''; this.exito = '';
    this.api.solicitar(this.seleccionado.id, this.cantidad, this.comentario).subscribe({
      next: solicitud => { this.exito = `Solicitud #${solicitud.id} registrada. Estado: ${solicitud.estado}. Revisa Mi panel.`; this.cargar(); },
      error: error => { this.error = error.error?.detail || error.error?.message || 'No se completó la solicitud. Revisa tu rol, disponibilidad y horario.'; }
    });
  }
  puedeSolicitar(lote: Lote): boolean {
    return this.auth.sesion?.rol === (lote.modalidad === 'VENTA' ? 'COMPRADOR' : 'ORGANIZACION');
  }
}
