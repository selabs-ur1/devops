import { Component, OnInit, Input } from '@angular/core';
import { PollCommentElement } from '../model/model';
import { PollService } from '../poll-service.service';

@Component({
  selector: 'app-show-comments',
  templateUrl: './show-comments.component.html',
  styleUrls: ['./show-comments.component.css']
})
export class ShowCommentsComponent implements OnInit {


  @Input()
  comments: PollCommentElement[];

  constructor() { }

  ngOnInit(): void {

  }

}
