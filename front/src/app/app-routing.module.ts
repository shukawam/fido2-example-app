import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginSuccessComponent } from './pages/login-success/login-success.component';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
import { SignInComponent } from './pages/sign-in/sign-in.component';
import { AuthGuard } from './guard/auth.guard';

const routes: Routes = [
  { path: '', component: SignUpComponent },
  {
    path: 'login-success',
    component: LoginSuccessComponent,
    canActivate: [AuthGuard],
  },
  // { path: 'sign-up', component: SignUpComponent },
  { path: 'sign-in', component: SignInComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
