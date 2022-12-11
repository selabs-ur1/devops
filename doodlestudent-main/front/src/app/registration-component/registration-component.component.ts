import { Component, OnInit, ViewChild } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { PollService } from '../poll-service.service';
import { FullCalendarComponent, CalendarOptions, EventInput } from '@fullcalendar/angular';
import frLocale from '@fullcalendar/core/locales/fr';
import { PollChoice, Poll, User } from '../model/model';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import * as myGlobals from 'globals';

@Component({
  selector: 'registration-poll-component',
  templateUrl: './registration-component.component.html',
  styleUrls: ['./registration-component.component.css'],
  providers: [PollService, MessageService]
})
export class RegistrationComponent implements OnInit {

  constructor(public messageService: MessageService, private pollService: PollService){}

  firstname = '';
  lastname = '';
  mail = '';
  password = '';

  ngOnInit() {}

  registration(): void {
    if (this.firstname && this.lastname && this.mail && this.password) {
      this.pollService.registration(this.firstname, this.lastname, this.mail, this.password).subscribe(e => {
        e;
      });
    }
  }

}
