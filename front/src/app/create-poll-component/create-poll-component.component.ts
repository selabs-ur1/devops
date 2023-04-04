import { Component, OnInit, ViewChild } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { PollService } from '../poll-service.service';
import { FullCalendarComponent, CalendarOptions, EventInput } from '@fullcalendar/angular';
import frLocale from '@fullcalendar/core/locales/fr';
import { PollChoice, Poll, User } from '../model/model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-create-poll-component',
  templateUrl: './create-poll-component.component.html',
  styleUrls: ['./create-poll-component.component.css'],
  providers: [MessageService, PollService, FullCalendarComponent]
})
export class CreatePollComponentComponent implements OnInit {
  urlsondage = '';
  urlsondageadmin = '';
  urlsalon = '';
  urlpad = '';

  items: MenuItem[];
  options: CalendarOptions;

  step = 0;

  slugid: string;
  poll: Poll = {};

  events: EventInput[] = [];
  eventsfromics: EventInput[] = [];
  allevents: EventInput[] = [];


  calendarComponent: FullCalendarComponent;
  hasics = false;
  loadics = false;
  ics: string;

  @ViewChild('calendar') set content(content: FullCalendarComponent) {
    if (content) { // initially setter gets called with undefined
      this.calendarComponent = content;
      const calendarApi = this.calendarComponent.getApi();

      this.poll.pollChoices.forEach(pc => {

        const evt =
        {
          title: '',
          start: pc.startDate,
          end: pc.endDate,
          resourceEditable: false,
          eventResizableFromStart: false,
          extendedProps: {
            choiceid: pc.id,
            tmpId: this.getUniqueId(8)
          },
        };
        this.events.push(evt);
        calendarApi.addEvent(evt, true);

      });
      calendarApi.setOption('validRange', {
        start: this.getValidDate(),
      });

    }
  }
  submitted = false;


  constructor(public messageService: MessageService, public pollService: PollService, private actRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.poll.pollChoices = [];
    this.items = [{
      label: 'Informations pour le rendez vous',
      command: () => {
        this.step = 0;
      }
    },
    {
      label: 'Choix de la date',
      command: () => {
        this.step = 1;
      }
    },
    {
      label: 'Résumé',
      command: () => {
        this.step = 2;
      }
    }
    ];



    this.options = {
      initialView: 'timeGridWeek',
      // dateClick: this.handleDateClick.bind(this), // bind is important!
      select: (selectionInfo) => {
        console.log(selectionInfo);
        const calendarApi = this.calendarComponent.getApi();
        console.log(this.getUniqueId(8));
        const evt = {
          title: '',
          start: selectionInfo.start,
          end: selectionInfo.end,
          resourceEditable: true,
          eventResizableFromStart: true,
          id: this.getUniqueId(8),

          extendedProps: {
//            tmpId: this.getUniqueId(8)
          },
        };
        calendarApi.addEvent(evt, true);
        this.events.push(evt);
        this.allevents.push(evt);
      },

      events: this.allevents,
      editable: true,
      droppable: true,
      //      selectMirror: true,
      eventResizableFromStart: true,
      selectable: true,
      locale: frLocale,
      themeSystem: 'bootstrap',
      slotMinTime: '08:00:00',
      slotMaxTime: '20:00:00',
      eventMouseEnter: (mouseEnterInfo) => {

      },
      eventDrop: (info) => {
        const evt = this.events.filter(e => e.id === info.event.id).pop();
        evt.start = info.event.start;
        evt.end = info.event.end;
      },
      eventResize: (info) => {
        const evt = this.events.filter(e => e.id === info.event.id).pop();
        const index = this.events.indexOf(evt);
        evt.start = info.event.start;
        evt.end = info.event.end;
      },
      eventClick: (info) => {
        const evt = this.events.filter(e => e.id === info.event.id).pop();
        if (evt != null){
        const index = this.events.indexOf(evt);
        if (index > -1) {
          this.events.splice(index, 1);
        }
        const index1 = this.allevents.indexOf(evt);
        if (index1 > -1) {
          this.allevents.splice(index1, 1);
        }
        info.event.remove();
      }

      },
      validRange: {
        start: Date.now()
      }
    };

    this.actRoute.paramMap.subscribe(params => {
      this.slugid = params.get('slugadminid');
      console.log(this.slugid);

      if (this.slugid != null) {

        this.pollService.getPollBySlugAdminId(this.slugid).subscribe(p => {
          if (p != null) {
            this.poll = p;
          } else {
            this.messageService.add(
              {
                severity: 'warn',
                summary: 'Un sondage avec cet identifiant n\'existe pas',
                detail: 'Le sondage n\'a pas été récupéré'
              }
            );
          }

        });
      }

    });



  }

  nextPage(): void {

    if (this.poll.title && this.poll.location && this.poll.description) {
      this.step = 1;


      return;
    }
    this.messageService.add(
      {
        severity: 'warn',
        summary: 'Données incomplètes',
        detail: 'Veuillez remplir les champs requis'
      }
    );

    this.submitted = true;
  }

  nextPage1(): void {
    console.log(this.poll.id);
    if (this.poll.id == null) {
      this.events.forEach(e => {
        this.poll.pollChoices.push({
          startDate: e.start as any,
          endDate: e.end as any,
        });
      });
      this.pollService.createPoll(this.poll).subscribe(p1 => {
        this.poll = p1;
        this.urlsondage = window.location.protocol + '//' + window.location.host + '/answer/' + p1.slug;
        this.urlsondageadmin = window.location.protocol + '//' + window.location.host + '/admin/' + p1.slugAdmin;
        this.urlsalon = p1.tlkURL;
        this.urlpad = p1.padURL;
        this.step = 2;
      });
    } else {

      const toKeep: PollChoice[] = [];
      this.events.filter(c => c.extendedProps != null && c.extendedProps.choiceid != null).forEach(e => {
        toKeep.push(this.poll.pollChoices.filter(c1 => c1.id === e.extendedProps.choiceid)[0]);
      });
      this.poll.pollChoices = toKeep;
      this.poll.pollChoices.forEach(c => {
        const res = this.events.filter(c1 => c1.extendedProps != null &&
          c1.extendedProps.choiceid != null && c1.extendedProps.choiceid === c.id)[0];
        c.startDate = res.start as any;
        c.endDate = res.end as any;
      });

      this.events.filter(c => c.extendedProps == null || c.extendedProps.choiceid == null).forEach(e => {
        this.poll.pollChoices.push({
          startDate: e.start as any,
          endDate: e.end as any,
        });
      });
      console.log(this.events);
      console.log(this.poll.pollChoices);

      this.pollService.updtatePoll(this.poll).subscribe(p1 => {
        this.poll = p1;
        this.urlsondage = 'http://localhost:4200/answer/' + p1.slug;
        this.urlsondageadmin = 'http://localhost:4200/admin/' + p1.slugAdmin;
        this.urlsalon = p1.tlkURL;
        this.urlpad = p1.padURL;
        this.step = 2;
      });


    }

  }

  prevPage1(): void {

    this.step = this.step - 1;
  }


  private getUniqueId(parts: number): string {
    const stringArr = [];
    for (let i = 0; i < parts; i++) {
      // tslint:disable-next-line:no-bitwise
      const S4 = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
      stringArr.push(S4);
    }
    return stringArr.join('-');
  }

  private getValidDate(): number {
    if (this.poll.id != null) {
      if ((this.poll.pollChoices[0].startDate as any - Date.now()) < 0) {
        return this.poll.pollChoices[0].startDate as any;
      }
    }
    return Date.now();

  }


  getICS(): void {
    this.loadics = true;
    this.pollService.getICS(this.slugid, this.ics).subscribe(res => {
      this.loadics = false;

      const calendarApi = this.calendarComponent.getApi();
      if (res.eventdtos.length > 0) {
        this.eventsfromics.forEach(eid => {
          const index = this.allevents.indexOf(eid);
          if (index > -1) {
            this.allevents.splice(index, 1);
          }
          calendarApi.getEventById(eid.id)?.remove();
        });
        this.eventsfromics = [];
      }
      console.log(res);

      res.eventdtos.forEach(evtdto => {      // calendarApi.next();
        const evt1 =
        {
          title: evtdto.description,
          start: evtdto.startDate,
          end: evtdto.endDate,
          resourceEditable: false,
          editable: false,
          droppable: false,
          selectable: false,
          eventResizableFromStart: false,
          id: this.getUniqueId(8),

          backgroundColor: 'red',
          extendedProps: {
            fromics: true
          },


        };
        const eventAPI = calendarApi.addEvent(evt1, true);
        this.eventsfromics.push(evt1);
        this.allevents.push(evt1);

      });

      const unselected = this.events.map(ev => ev.extendedProps.choiceid);
      res.selectedChoices.forEach(e => {
        const index = unselected.indexOf(e);
        if (index > -1) {
          unselected.splice(index, 1);
        }
        const evt1 = this.events.filter(ev => ev.extendedProps.choiceid === e)[0];

        const evt2 = calendarApi.getEventById(evt1.id);
        evt1.backgroundColor = 'red';
        evt1.extendedProps.selected = false;
        evt2.setProp('backgroundColor', 'red');
//        this.poll.pollChoices.filter(pc => pc.id === evt1.extendedProps.choiceid)[0].users.push({ id: -1 });
      });
      unselected.forEach(e => {
        const evt1 = this.events.filter(ev => ev.extendedProps.choiceid === e)[0];

        const evt2 = calendarApi.getEventById(evt1.id);
        evt1.backgroundColor = 'green';
        evt1.extendedProps.selected = true;
        evt2.setProp('backgroundColor', 'green');
        this.poll.pollChoices.filter(pc => pc.id === evt1.extendedProps.choiceid)[0].users.push({ id: -1 });
      });
    }, (err) => {
      this.loadics = false;

      this.messageService.add(
        {
          severity: 'warn',
          summary: 'Ne peut récupérer l\'agenda à partir de l\'adresse de l\'ics',
          detail: 'Une erreur s\'est produite au moment de la récupération de l\'agenda'
        }
      );
    }
    );

  }
}
