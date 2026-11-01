// Interfaces básicas: mantienen los nombres de los campos del backend Java (DTO y entidades).
export type Rol = 'COMPRADOR' | 'VENDEDOR' | 'ORGANIZACION' | 'ADMIN';
export type Modalidad = 'VENTA' | 'DONACION';

export interface UsuarioSesion {
  token: string;
  username: string;
  rol: Rol;
}
export interface PerfilUsuario {
  id: number;
  username: string;
  rol: Rol;
  nombre: string;
  verificado: boolean;
}
export interface RegistroUsuario {
  username: string;
  password: string;
  nombreCompleto: string;
  correo: string;
  telefono: string;
  rol: Rol;
}
export interface Lote {
  id: number;
  nombre: string;
  categoria: string;
  descripcion: string;
  alergenos: string;
  imagenUrl: string | null;
  modalidad: Modalidad;
  stockInicial: number;
  stockDisponible: number;
  stockReservado: number;
  precioOriginal: number;
  precioLiquidacion: number;
  pesoPorUnidadKg: number;
  conservacion: string;
  fechaVencimiento: string;
  limiteReserva: string;
  limiteRecojo: string;
  estado: string;
  estadoInocuidad: string;
  comercio: string;
  establecimientoId: number;
  distrito: string;
  direccion: string;
  latitud?: number;
  longitud?: number;
  propietarioId: number;
  politicaAsignacion: string;
}
export interface Establecimiento {
  id: number;
  nombreComercial: string;
  direccion: string;
  distrito: string;
  telefono: string;
  correo: string;
  usuarioResponsableId: number;
}
export interface EstablecimientoFormulario {
  nombreComercial: string;
  direccion: string;
  distrito: string;
  telefono: string;
  correo: string;
  horarioAtencion: string;
  horarioRecojo: string;
  ruc: string;
  nombreResponsable: string;
  telefonoResponsable: string;
  minutosPreparacion: number;
}
export interface LoteFormulario {
  establecimientoId: number;
  nombre: string;
  categoria: string;
  descripcion: string;
  ingredientes: string;
  alergenos: string;
  imagenUrl: string;
  modalidad: Modalidad;
  cantidad: number;
  precioOriginal: number;
  precioLiquidacion: number;
  pesoPorUnidadKg: number;
  fechaPreparacion: string | null;
  fechaVencimiento: string;
  limiteReserva: string;
  limiteRecojo: string;
  conservacion: string;
  politicaAsignacion: string;
}
export interface Solicitud {
  id: number;
  loteId: number;
  solicitanteId: number;
  propietarioId: number;
  tipo: Modalidad;
  cantidadSolicitada: number;
  cantidadAsignada: number;
  estado: string;
  codigoRetiro: string | null;
  creadaEn: string;
  comentario: string;
}
export interface Entrega {
  id: number;
  solicitudId: number;
  estado: string;
  origen: string;
  destino: string;
  horaEstimada: string;
  listaEn: string;
  entregadaEn: string;
}
export interface Organizacion {
  id?: number;
  razonSocial: string;
  identificacion: string;
  direccion: string;
  distrito: string;
  contacto: string;
  capacidadRaciones: number;
  transportePropio: boolean;
  refrigeracion: boolean;
  verificada?: boolean;
}
export interface Notificacion {
  id: number;
  titulo: string;
  mensaje: string;
  fecha: string;
  leida: boolean;
}
export interface Incidencia {
  id: number;
  solicitudId: number | null;
  tipo: string;
  descripcion: string;
  estado: string;
  resolucion: string;
  fecha: string;
}
export interface Metrica {
  entregasConfirmadas: number;
  unidadesEntregadas: number;
  ingresosRecuperados: number;
  lotesPublicados: number;
  solicitudes: number;
}
export interface Estimacion {
  disponible: boolean;
  mensaje?: string;
  tipo?: string;
  promedioUnidadesPorVenta?: number;
  muestra?: number;
}
export interface Conservacion {
  id: number;
  fecha: string;
  temperaturaCelsius: number;
  observaciones: string;
}
