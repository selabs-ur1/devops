import { Component, OnInit, Input } from '@angular/core';
import { CarpoolingProposition } from '../model/model';
import { PollService } from '../poll-service.service';
import { AnswerPollComponent } from '../answer-poll/answer-poll.component';

@Component({
  selector: 'app-show-carpooling',
  templateUrl: './carpooling-component.component.html',
  styleUrls: ['./carpooling-component.component.css'],
  providers: [PollService]
})
export class CarpoolingComponent implements OnInit {


  @Input()
  carpoolingPropositions: CarpoolingProposition[];

  constructor(private pollService: PollService, private answerPollComponent: AnswerPollComponent) { }

  joinedCarpooling = false;

  ngOnInit(): void {

  }

  joinCarpooling(carpoolingId: number) : void {
    if (!this.joinedCarpooling) {
      console.log("joinCarpooling(" + carpoolingId + ") - CarpoolingComponent.ts");
      this.joinedCarpooling = this.answerPollComponent.joinCarpooling(carpoolingId);
    }

    //return this.http.put<CarpoolingProposition>('/poll/carpooling/'+carpoolingId, carpooling);
  }

}
