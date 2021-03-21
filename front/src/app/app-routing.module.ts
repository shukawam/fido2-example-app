import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginSuccessComponent } from './pages/login-success/login-success.component';
import { SignupComponent } from './pages/signup/signup.component';
import { HomeComponent } from './pages/home/home.component';
import { SignInComponent } from './pages/sign-in/sign-in.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login-success', component: LoginSuccessComponent },
  { path: 'sign-up', component: SignupComponent },
  { path: 'sign-in', component: SignInComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
