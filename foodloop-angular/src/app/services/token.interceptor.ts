import { HttpInterceptorFn } from '@angular/common/http';

// Adjunta el token JWT solo a las peticiones HTTP de esta aplicación.
export const tokenInterceptor: HttpInterceptorFn = (request, next) => {
  const guardado = sessionStorage.getItem('foodloop_sesion');
  if (guardado) {
    try {
      const token: string = (JSON.parse(guardado) as { token: string }).token;
      if (token) request = request.clone({ setHeaders: { Authorization: 'Bearer ' + token } });
    } catch { /* Sesión no válida: la API responderá 401. */ }
  }
  return next(request);
};
