import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { PerfilUsuario, RegistroUsuario, UsuarioSesion } from '../models/foodloop.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private sesionActual = new BehaviorSubject<UsuarioSesion | null>(this.leerSesion());
  sesion$ = this.sesionActual.asObservable();

  constructor(private http: HttpClient) {}

  private leerSesion(): UsuarioSesion | null {
    try {
      const guardado = sessionStorage.getItem('foodloop_sesion');
      return guardado ? JSON.parse(guardado) as UsuarioSesion : null;
    } catch { return null; }
  }

  get sesion(): UsuarioSesion | null { return this.sesionActual.value; }
  get token(): string { return this.sesion?.token || ''; }

  iniciar(username: string, password: string): Observable<UsuarioSesion> {
    return this.http.post<UsuarioSesion>('/auth/session', { username, password }).pipe(
      tap(sesion => {
        sessionStorage.setItem('foodloop_sesion', JSON.stringify(sesion));
        this.sesionActual.next(sesion);
      })
    );
  }

  registrar(datos: RegistroUsuario): Observable<string> {
    return this.http.post('/auth/register', datos, { responseType: 'text' });
  }

  miPerfil(): Observable<PerfilUsuario> { return this.http.get<PerfilUsuario>('/auth/me'); }

  salir(): void {
    sessionStorage.removeItem('foodloop_sesion');
    this.sesionActual.next(null);
  }
}
