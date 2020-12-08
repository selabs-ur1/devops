import { Component } from '@angular/core';
import { HomeComponentComponent } from './home-component/home-component.component';
import { CreatePollComponentComponent } from './create-poll-component/create-poll-component.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [HomeComponentComponent, CreatePollComponentComponent]
})
export class AppComponent {
}
