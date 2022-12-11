import { Component, OnInit, Input } from '@angular/core';
import { Card } from '../home-component/Card';

@Component({
  selector: 'app-card-small-component',
  templateUrl: './card-small-component.component.html',
  styleUrls: ['./card-small-component.component.css']
})
export class CardSmallComponentComponent implements OnInit {


  @Input()
  cards: Card[];

  constructor() { }

  ngOnInit(): void {
  }

}
