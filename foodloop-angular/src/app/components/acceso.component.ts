import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../services/auth.service';
import { RegistroUsuario, Rol } from '../models/foodloop.models';

@Component({
  selector: 'app-acceso', standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  templateUrl: './acceso.component.html'
})
export class AccesoComponent {
  registro = false;
  username = '';
  password = '';
  datos: RegistroUsuario = { username: '', password: '', nombreCompleto: '', correo: '', telefono: '', rol: 'COMPRADOR' };
  roles: Rol[] = ['COMPRADOR', 'VENDEDOR', 'ORGANIZACION'];
  error = ''; mensaje = ''; cargando = false;
  constructor(private auth: AuthService, private router: Router) {}
  entrar(): void {
    this.error = ''; this.cargando = true;
    this.auth.iniciar(this.username, this.password).subscribe({
      next: () => { this.cargando = false; this.router.navigateByUrl('/panel'); },
      error: () => { this.cargando = false; this.error = 'Usuario o contraseña incorrectos, o servidor no disponible.'; }
    });
  }
  crear(): void {
    this.error = ''; this.mensaje = '';
    if (this.datos.password.length < 8) { this.error = 'La contraseña debe tener mínimo 8 caracteres.'; return; }
    this.cargando = true;
    this.auth.registrar(this.datos).subscribe({
      next: () => { this.registro = false; this.cargando = false; this.mensaje = 'Cuenta registrada. Inicia sesión con tus credenciales.'; this.username = this.datos.username; },
      error: error => { this.cargando = false; this.error = error.error?.detail || 'No se pudo registrar el usuario.'; }
    });
  }
}
