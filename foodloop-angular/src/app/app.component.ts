import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet, Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, MatToolbarModule, MatButtonModule],
  template: `
    <header class="topbar">
      <nav class="container nav-wrap" aria-label="Navegación principal">
        <a routerLink="/" class="brand"><span class="brand-symbol">◌</span> Food<span>Loop</span></a>
        <div class="nav-links">
          <a routerLink="/">Inicio</a>
          <a routerLink="/catalogo">Alimentos</a>
          <a routerLink="/" fragment="como-funciona">Cómo funciona</a>
        </div>
        <div class="nav-actions">
          <a mat-button routerLink="/panel" *ngIf="auth.sesion">Mi panel</a>
          <a mat-flat-button routerLink="/acceso" *ngIf="!auth.sesion">Ingresar</a>
          <button mat-button *ngIf="auth.sesion" (click)="salir()">Salir</button>
        </div>
      </nav>
    </header>
    <main><router-outlet></router-outlet></main>
    <footer class="site-footer">
      <div class="container footer-grid">
        <div><strong>FoodLoop</strong><p>Conectamos excedentes alimentarios con nuevas oportunidades.</p></div>
        <div><a routerLink="/catalogo">Explorar alimentos</a><a routerLink="/acceso">Crear cuenta</a></div>
        <div><small>Proyecto universitario • Versión de desarrollo • SaVe</small></div>
      </div>
    </footer>
  `
})
export class AppComponent {
  constructor(public auth: AuthService, private router: Router) {}
  salir(): void { this.auth.salir(); this.router.navigateByUrl('/'); }
}
