import { Component, OnInit, ViewChild } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { PollService } from '../poll-service.service';
import { Poll, User, PollChoice, PollCommentElement, ChoiceUser } from '../model/model';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import { FullCalendarComponent } from '@fullcalendar/angular';

@Component({
  selector: 'app-admin-poll',
  templateUrl: './admin-poll.component.html',
  styleUrls: ['./admin-poll.component.css'],
  providers: [MessageService, PollService, FullCalendarComponent]

})
export class AdminPollComponent implements OnInit {

  constructor(public messageService: MessageService, private actRoute: ActivatedRoute, private pollService: PollService) { }
  slugid: string;
  poll: Poll;
  events: EventInput[] = [];
  uniqueUsers: User[] = [];
  userChoices: Map<number, PollChoice[]> = new Map();
  comments: PollCommentElement[];

  ngOnInit(): void {
    this.actRoute.paramMap.subscribe(params => {
      this.slugid = params.get('slugadminid');
      this.pollService.getPollBySlugAdminId(this.slugid).subscribe(p => {
        this.poll = p;
        if (p != null){
          this.pollService.getComentsBySlugId(this.poll?.slug).subscribe(cs => this.comments = cs);
        }
        this.uniqueUsers.splice(0, this.uniqueUsers.length);
        this.poll.pollChoices.forEach(pc => {
          pc.users.forEach(user => {
            if (this.uniqueUsers.filter(us => us.id === user.id).length  === 0 ){
              this.uniqueUsers.push(user);
              this.userChoices.set(user.id, []);
            }
          });

          const evt =
          {
            title: '',
            start: pc.startDate,
            end: pc.endDate,
            resourceEditable: false,
            eventResizableFromStart: false,
            backgroundColor: 'red',
            extendedProps: {
              choiceid: pc.id,
              selected: false
            },
          };
          this.events.push(evt);
        });
        this.poll.pollChoices.forEach(pc => {
          pc.users.forEach(us => {
              this.userChoices.get(us.id).push(pc);
          });
        });

      });
    });


  }


  selectEvent($event: any, event: EventInput): void{
    this.pollService.selectEvent(event.extendedProps.choiceid).subscribe(e => {
      this.messageService.add({
        severity: 'success',
        summary: 'Données enregistrées',
        detail: 'Le sondage est maintenant close'}
        );
      this.poll.clos = true;
    }, (error) => {
      this.messageService.add(
        {
          severity: 'warn',
          summary: 'Sélection de cette date impossible',
          detail: 'Le sondage n\'a pu être clos'}
          );
    });

    return;

  }




}
