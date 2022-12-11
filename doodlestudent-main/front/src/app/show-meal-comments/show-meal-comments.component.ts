import { Component, OnInit, Input } from '@angular/core';
import { PollCommentElement } from '../model/model';
import { PollService } from '../poll-service.service';

@Component({
  selector: 'app-show-meal-comments',
  templateUrl: './show-meal-comments.component.html',
  styleUrls: ['./show-meal-comments.component.css']
})
export class ShowMealCommentsComponent implements OnInit {


  @Input()
  mealPreferenceComments: PollCommentElement[];

  constructor() { }

  ngOnInit(): void {

  }

}
