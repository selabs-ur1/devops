import { Component, OnInit, ViewChild } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { PollService } from '../poll-service.service';
import { FullCalendarComponent, CalendarOptions, EventInput } from '@fullcalendar/angular';
import frLocale from '@fullcalendar/core/locales/fr';
import { PollChoice, Poll, User, Account } from '../model/model';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import * as myGlobals from 'globals';

@Component({
  selector: 'connection-poll-component',
  templateUrl: './connection-component.component.html',
  styleUrls: ['./connection-component.component.css'],
  providers: [PollService, MessageService]
})
export class ConnectionComponent implements OnInit {

  constructor(public messageService: MessageService, private pollService: PollService){}

  mail = '';
  password = '';

  ngOnInit() {}

  connection(): void {
    if (this.mail && this.password) {
      this.pollService.connection(this.mail, this.password).subscribe(e => {
        console.log("connexion");
        e;
      });
    }
  }
}
