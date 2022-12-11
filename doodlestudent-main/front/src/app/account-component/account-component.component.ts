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
  selector: 'account-poll-component',
  templateUrl: './account-component.component.html',
  styleUrls: ['./account-component.component.css'],
  providers: [PollService, MessageService]
})
export class AccountComponent implements OnInit {

  constructor(public messageService: MessageService, private pollService: PollService){}

  account: Account;
  pollsAdmin: Poll[];
  pollsMember: Poll[];
  ics = '';

  ngOnInit(): void {
    this.pollService.getAccount().subscribe(e => {
      this.account = e;
      
      this.pollService.getPollAdmin(this.account.mail).subscribe(a => {
        this.pollsAdmin = a;
      });

      this.pollService.getPollMember(this.account.mail).subscribe(m => {
        this.pollsMember = m;
      });
    });
  }

  changeIcs(): void {
    this.pollService.changeIcs(this.account.mail, this.ics).subscribe(e => {
      e;
    });
  }
}
