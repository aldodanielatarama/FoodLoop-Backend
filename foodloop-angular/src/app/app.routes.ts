import { Routes } from '@angular/router';
import { InicioComponent } from './components/inicio.component';
import { CatalogoComponent } from './components/catalogo.component';
import { AccesoComponent } from './components/acceso.component';
import { PanelComponent } from './components/panel.component';

export const routes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'catalogo', component: CatalogoComponent },
  { path: 'acceso', component: AccesoComponent },
  { path: 'panel', component: PanelComponent },
  { path: '**', redirectTo: '' }
];
