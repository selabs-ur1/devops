import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnswerPollComponent } from './answer-poll.component';

describe('AnswerPollComponent', () => {
  let component: AnswerPollComponent;
  let fixture: ComponentFixture<AnswerPollComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnswerPollComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnswerPollComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
