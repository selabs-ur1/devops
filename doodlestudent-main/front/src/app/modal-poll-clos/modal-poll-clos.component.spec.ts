import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalPollClosComponent } from './modal-poll-clos.component';

describe('ModalPollClosComponent', () => {
  let component: ModalPollClosComponent;
  let fixture: ComponentFixture<ModalPollClosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalPollClosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalPollClosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
