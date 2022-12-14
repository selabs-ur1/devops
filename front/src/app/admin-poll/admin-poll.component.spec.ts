import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPollComponent } from './admin-poll.component';

describe('AdminPollComponent', () => {
  let component: AdminPollComponent;
  let fixture: ComponentFixture<AdminPollComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminPollComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminPollComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
