import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoordinatorClubDetailsComponent } from './coordinator-club-details.component';

describe('CoordinatorClubDetailsComponent', () => {
  let component: CoordinatorClubDetailsComponent;
  let fixture: ComponentFixture<CoordinatorClubDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoordinatorClubDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoordinatorClubDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
