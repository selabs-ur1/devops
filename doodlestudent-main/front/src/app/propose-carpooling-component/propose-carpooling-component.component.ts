import { Component, OnInit, Input } from '@angular/core';
import { CarpoolingProposition } from '../model/model';
import { PollService } from '../poll-service.service';
import { AnswerPollComponent } from '../answer-poll/answer-poll.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-show-propose-carpooling',
  templateUrl: './propose-carpooling-component.component.html',
  styleUrls: ['./propose-carpooling-component.component.css'],
  providers: [PollService, NgbModal]
})
export class ProposeCarpoolingComponent implements OnInit {

  constructor(private pollService: PollService, private answerPollComponent: AnswerPollComponent, private ngbModal: NgbModal) { }

  carpoolingInfos: any = {
    driver: '',
    departure_localisation: '',
    departure_time: '',
    arriving_time: '',
    places: ''
  };

  ngOnInit(): void {

  }

  proposeCarpooling() : void {
    //if (!this.joinedCarpooling) {
      console.log("proposeCarpooling() - ProposeCarpoolingComponent.ts");
      if (this.carpoolingInfos.driver && this.carpoolingInfos.departure_localisation &&
        this.carpoolingInfos.departure_time && this.carpoolingInfos.arriving_time && this.carpoolingInfos.places) {

      this.answerPollComponent.proposeCarpooling(this.carpoolingInfos.driver, this.carpoolingInfos.departure_localisation,
        this.carpoolingInfos.departure_time, this.carpoolingInfos.arriving_time, this.carpoolingInfos.places);
    }
    //}

    //return this.http.put<CarpoolingProposition>('/poll/carpooling/'+carpoolingId, carpooling);
  }

}
