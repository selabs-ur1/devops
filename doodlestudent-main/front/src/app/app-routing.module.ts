import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponentComponent } from './home-component/home-component.component';
import { CreatePollComponentComponent } from './create-poll-component/create-poll-component.component';
import { AnswerPollComponent } from './answer-poll/answer-poll.component';
import { AdminPollComponent } from './admin-poll/admin-poll.component';
import { ConnectionComponent } from './connection-component/connection-component.component';
import { RegistrationComponent } from './registration-component/registration-component.component';
import { AccountComponent } from './account-component/account-component.component';
import { LoginComponent } from './login/login.component';
import { AuthGuardService } from './auth-guard.service';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'home',
    component: HomeComponentComponent
  },
  {
    path: 'create',
    component: CreatePollComponentComponent
  },
  {
    path: 'update/:slugadminid',
    component: CreatePollComponentComponent
  },
  {
    path: 'answer/:slugid',
    component: AnswerPollComponent
  },
  {
    path: 'admin/:slugadminid',
    component: AdminPollComponent
  },
  {
    path: 'connection',
    component: ConnectionComponent
  },
  {
    path: 'registration',
    component: RegistrationComponent
  },
  {
    path: 'account',
    component: AccountComponent
  }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
