import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardSmallComponentComponent } from './card-small-component.component';

describe('CardSmallComponentComponent', () => {
  let component: CardSmallComponentComponent;
  let fixture: ComponentFixture<CardSmallComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CardSmallComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardSmallComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
