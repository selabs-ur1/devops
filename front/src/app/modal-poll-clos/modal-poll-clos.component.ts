import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Poll } from '../model/model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-modal-poll-clos',
  templateUrl: './modal-poll-clos.component.html',
  styleUrls: ['./modal-poll-clos.component.css'],
  providers: [NgbActiveModal]
})
export class ModalPollClosComponent implements OnInit {

  @Input() poll: Poll;
  constructor(public activeModal: NgbActiveModal, public router: Router) { }

  ngOnInit(): void {
  }

  dismissModalAndNavigate(): void{
    this.activeModal.close();
    window.location.href = '/';

  }
}
