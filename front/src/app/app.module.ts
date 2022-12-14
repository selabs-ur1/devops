import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CardSmallComponentComponent } from './card-small-component/card-small-component.component';
import { HomeComponentComponent } from './home-component/home-component.component';
import { CreatePollComponentComponent } from './create-poll-component/create-poll-component.component';
import {StepsModule} from 'primeng/steps';
import {MenuItem} from 'primeng/api';
// import {FullCalendarModule} from 'primeng/fullcalendar';
import {ToastModule} from 'primeng/toast';
import {MessagesModule} from 'primeng/messages';
import {MessageModule} from 'primeng/message';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {InputSwitchModule} from 'primeng/inputswitch';
import {CardModule} from 'primeng/card';
import {ButtonModule} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {SelectButtonModule} from 'primeng/selectbutton';
import {MenubarModule} from 'primeng/menubar';
import {CheckboxModule} from 'primeng/checkbox';

import dayGridPlugin from '@fullcalendar/daygrid'; // a plugin
import interactionPlugin from '@fullcalendar/interaction'; // a plugin
import timeGridPlugin from '@fullcalendar/timegrid';

import { FullCalendarModule } from '@fullcalendar/angular';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AnswerPollComponent } from './answer-poll/answer-poll.component';
import { AdminPollComponent } from './admin-poll/admin-poll.component';
import { DateagoPipe } from './dateago.pipe'; // the main connector. must go first

import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import { UsernamePipePipe } from './username-pipe.pipe';
import { Selecteddate4userPipePipe } from './selecteddate4user-pipe.pipe';
import { ModalPollClosComponent } from './modal-poll-clos/modal-poll-clos.component';
import { TopBarComponent } from './top-bar/top-bar.component';
import { ShowCommentsComponent } from './show-comments/show-comments.component';
registerLocaleData(localeFr, 'fr');
FullCalendarModule.registerPlugins([ // register FullCalendar plugins
  dayGridPlugin,
  interactionPlugin,
  timeGridPlugin
]);
@NgModule({
  declarations: [
    AppComponent,
    CardSmallComponentComponent,
    HomeComponentComponent,
    CreatePollComponentComponent,
    AnswerPollComponent,
    AdminPollComponent,
    DateagoPipe,
    UsernamePipePipe,
    Selecteddate4userPipePipe,
    ModalPollClosComponent,
    TopBarComponent,
    ShowCommentsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    StepsModule,
    FullCalendarModule,
    ToastModule,
    MessagesModule,
    MessageModule,
    InputSwitchModule,
    CardModule,
    ButtonModule,
    InputTextModule,
    InputTextareaModule,
    SelectButtonModule,
    MenubarModule,
    CheckboxModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent],


})
export class AppModule { }
