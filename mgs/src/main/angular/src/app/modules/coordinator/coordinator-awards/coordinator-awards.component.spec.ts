import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoordinatorAwardsComponent } from './coordinator-awards.component';

describe('CoordinatorAwardsComponent', () => {
  let component: CoordinatorAwardsComponent;
  let fixture: ComponentFixture<CoordinatorAwardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoordinatorAwardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoordinatorAwardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
