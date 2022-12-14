import { TestBed } from '@angular/core/testing';

import { ABTestingService } from './abtesting.service';

describe('ABTestingService', () => {
  let service: ABTestingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ABTestingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
