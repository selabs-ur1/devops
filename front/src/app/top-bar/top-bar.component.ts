import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent implements OnInit {


  @Input()
  padURL: string ;

  @Input()
  talkToURL: string ;

  @Input()
  adminSlug: string;

  @Input()
  slug: string;


  constructor() { }

  ngOnInit(): void {
  }

  copyMessage(): void {
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = window.location.protocol + '//' + window.location.host + '/answer/' + this.slug;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
  }

  getExcelUrl(): string {
    return window.location.protocol + '//' + window.location.host + '/api/polls/' + this.slug + '/results';
  }

}
