import { Component, OnInit, ViewChild, AfterViewChecked } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { stringify } from 'querystring';
import { PollService } from '../poll-service.service';
import { Poll, ChoiceUser, PollCommentElement, User, PollChoice } from '../model/model';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import { FullCalendarComponent } from '@fullcalendar/angular';
import frLocale from '@fullcalendar/core/locales/fr';
import { MessageService } from 'primeng/api';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalPollClosComponent } from '../modal-poll-clos/modal-poll-clos.component';

@Component({
  selector: 'app-answer-poll',
  templateUrl: './answer-poll.component.html',
  styleUrls: ['./answer-poll.component.css'],
  providers: [MessageService, PollService, FullCalendarComponent, NgbModal]

})
export class AnswerPollComponent implements OnInit {

  constructor(public messageService: MessageService,
    // tslint:disable-next-line:align
    private actRoute: ActivatedRoute, private pollService: PollService,
    // tslint:disable-next-line:align
    private modalService: NgbModal) { }
  slugid: string;
  poll: Poll;
  calendarortableoption: any[];
  calendarortable = 'calendar';
  personalInformation: any = {
    nom: '',
    mail: '',
    desc: '',
    ics: '',
    pref: false
  };
  hasics: false;
  options: CalendarOptions;
  @ViewChild('calendar') calendarComponent: FullCalendarComponent;
  submitted = false;
  csubmitted = false;
  voeuxsoumis = false;
  commentsoumis = false;
  events: EventInput[] = [];
  eventsfromics: EventInput[] = [];
  allevents: EventInput[] = [];
  loadics = false;
  loademail = false;
  comments: PollCommentElement[];

  comment1 = '';
  commentdesc1 = '';
  uniqueUsers: User[] = [];
  userChoices: Map<number, PollChoice[]> = new Map();
  ngOnInit(): void {
    this.calendarortableoption = [
      { icon: 'pi pi-calendar', text: 'Calendrier', value: 'calendar' },
      { icon: 'pi pi-table', text: 'Tableau', value: 'table' },
    ];


    this.actRoute.paramMap.subscribe(params => {
      this.slugid = params.get('slugid');
      this.pollService.getPollBySlugId(this.slugid).subscribe(p => {
        this.poll = p;
        this.pollService.getComentsBySlugId(this.slugid).subscribe(cs => this.comments = cs);


        if (this.poll.clos) {
          this.openModal();
        }
        const calendarApi = this.calendarComponent.getApi();
        // calendarApi.next();
        this.uniqueUsers.splice(0, this.uniqueUsers.length);
        this.poll.pollChoices.forEach(pc => {
          pc.users.forEach(user => {
            if (this.uniqueUsers.filter(us => us.id === user.id).length === 0) {
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
            id: this.getUniqueId(8),
            extendedProps: {
              choiceid: pc.id,
              selected: false,
            },
          };
          calendarApi.addEvent(evt, true);
          this.events.push(evt);
          this.allevents.push(evt);

        });
        this.poll.pollChoices.forEach(pc => {
          pc.users.forEach(us => {
            this.userChoices.get(us.id).push(pc);
          });
        });

      });
    });

    this.options = {
      initialView: 'timeGridWeek',
      // dateClick: this.handleDateClick.bind(this), // bind is important!
      /*eventDragStart: (timeSheetEntry, jsEvent, ui, activeView) => {
        this.eventDragStart(
            timeSheetEntry, jsEvent, ui, activeView
        );
     },
eventDragStop: (timeSheetEntry, jsEvent, ui, activeView) => {
        this.eventDragStop(
           timeSheetEntry, jsEvent, ui, activeView
        );
      },*/
      //      events: this.events,
      events: this.allevents,
      editable: false,
      droppable: false,
      //      selectMirror: true,
      eventResizableFromStart: false,
      selectable: false,
      locale: frLocale,
      themeSystem: 'bootstrap',
      slotMinTime: '08:00:00',
      slotMaxTime: '20:00:00',
      eventMouseEnter: (mouseEnterInfo) => {

      },
      eventClick: (info) => {
        if (!info.event.extendedProps.fromics) {
          if (info.event.extendedProps.selected) {
            info.event.setExtendedProp('selected', false);
            const evt = this.events.filter(e => e.extendedProps.choiceid === info.event.extendedProps.choiceid).pop();
            evt.extendedProps.selected = false;
            evt.backgroundColor = 'red';
            info.event.setProp('backgroundColor', 'red');
            this.poll.pollChoices.filter(pc => pc.id === evt.extendedProps.choiceid)[0].users.splice(-1, 1);

          } else {
            info.event.setExtendedProp('selected', true);
            const evt = this.events.filter(e => e.extendedProps.choiceid === info.event.extendedProps.choiceid).pop();
            evt.extendedProps.selected = true;
            evt.backgroundColor = 'green';
            info.event.setProp('backgroundColor', 'green');
            this.poll.pollChoices.filter(pc => pc.id === evt.extendedProps.choiceid)[0].users.push({ id: -1 });

          }
        }

        //        info.event.remove();
      },
    };


  }


  updateEvent($event: any, event: EventInput): void {

    event.extendedProps.selected = $event.checked;
    if ($event.checked) {
      event.backgroundColor = 'green';
      this.poll.pollChoices.filter(pc => pc.id === event.extendedProps.choiceid)[0].users.push({ id: -1 });

    } else {
      event.backgroundColor = 'red';
      this.poll.pollChoices.filter(pc => pc.id === event.extendedProps.choiceid)[0].users.splice(-1, 1);


    }
  }

  createComment(): void {


    if (this.comment1 && this.commentdesc1) {
      const c: PollCommentElement = {
        content: this.commentdesc1,
        auteur: this.comment1
      };
      this.pollService.addComment4Poll(this.slugid, c).subscribe(e => {
        this.messageService.add({
          severity: 'success',
          summary: 'Données enregistrées',
          detail: 'Merci pour ce commentaire'
        }
        );
        this.pollService.getComentsBySlugId(this.poll?.slug).subscribe(cs => this.comments = cs);
        this.commentsoumis = true;
      });

      return;
    }
    this.messageService.add(
      {
        severity: 'warn',
        summary: 'Données incomplètes',
        detail: 'Veuillez remplir les champs requis'
      }
    );
    this.csubmitted = true;
  }

  createReponse(): void {
    if (this.personalInformation.nom && this.personalInformation.mail &&
      this.events.filter(e => e.extendedProps.selected).length > 0 &&
      (this.personalInformation.desc || !this.personalInformation.pref)) {
      const cu: ChoiceUser = {
        username: this.personalInformation.nom,
        mail: this.personalInformation.mail,
        pref: this.personalInformation.desc,
        ics: this.personalInformation.ics,
        choices: this.events.filter(e => e.extendedProps.selected).map(x => x.extendedProps.choiceid)
      };
      this.pollService.updateChoice4user(cu).subscribe(e => {
        //  cu.choices.forEach(c => this.poll.pollChoices.filter( c1 => c1.id === c)[0].users.push(e));
        //  if (this.uniqueUsers.filter(u1 => u1.id === e.id ).length === 0) {
        //    this.uniqueUsers.push(e);
        // }
        this.messageService.add({
          severity: 'success',
          summary: 'Données enregistrées',
          detail: 'Merci pour votre participation'
        }
        );
        this.voeuxsoumis = true;
      });
      return;
    }
    this.messageService.add(
      {
        severity: 'warn',
        summary: 'Données incomplètes',
        detail: 'Veuillez remplir les champs requis et sélectioner au moins une date'
      }
    );
    this.submitted = true;

  }

  getICS(): void {
    this.loadics = true;
    this.pollService.getICS(this.slugid, this.personalInformation.ics).subscribe(res => {
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
          eventResizableFromStart: false,
          id: this.getUniqueId(8),

          backgroundColor: 'blue',
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


  openModal(): void {
    const modalRef = this.modalService.open(ModalPollClosComponent, {
      beforeDismiss: () => false,
      centered: true,
      windowClass: 'lgModal',
      backdrop: 'static'
    });
    modalRef.componentInstance.poll = this.poll;
  }

  getUserFromMail(): void {
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



}
