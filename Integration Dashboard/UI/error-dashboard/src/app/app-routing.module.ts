import { AuthGuard } from './helper/auth.guard';
import { ViewErrorDataComponent } from './view-error-data/view-error-data.component';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { HomeComponent } from './home/home.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  { path: 'home', component: HomeComponent,canActivate: [AuthGuard]  },
  { path: 'login', component: LoginComponent },
  { path: 'view-error-data', component: ViewErrorDataComponent,canActivate: [AuthGuard]  },
  { path: '', redirectTo: '/home', pathMatch: 'full', canActivate: [AuthGuard]  },
  { path: '**', component: PageNotFoundComponent, canActivate: [AuthGuard]  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
