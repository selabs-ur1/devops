import { Component, OnInit } from '@angular/core';
import { CardSmallComponentComponent } from '../card-small-component/card-small-component.component';
import {Card} from './Card';
@Component({
  selector: 'app-home-component',
  templateUrl: './home-component.component.html',
  styleUrls: ['./home-component.component.css'],
  providers: [CardSmallComponentComponent]

})
export class HomeComponentComponent implements OnInit {

  constructor() { }


  cards: Card[] = [];

  ngOnInit(): void {

    this.cards.push(new Card('assets/1.png', {backgroundColor: '#44baf2', color: 'white'}, 'Créez un sondage', 'Définissez plusieurs créneaux pour votre réunion.'));
    this.cards.push(new Card('assets/2.png', {backgroundColor: '#fc506d', color: 'white'}, 'Envoyez vos invitations', 'Les participants aux sondages pourront voter pour les dates qui leur conviennent le mieux !'));
    this.cards.push(new Card('assets/3.png', {backgroundColor: '#8f3ee8', color: 'white'}, 'Faites votre choix', 'Vous pourrez obtenir en direct les résultats du sondage afin de choisir au mieux la meilleure proposition.'));


  }

}
