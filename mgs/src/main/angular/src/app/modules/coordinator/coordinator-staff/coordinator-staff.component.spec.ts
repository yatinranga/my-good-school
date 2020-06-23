import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoordinatorStaffComponent } from './coordinator-staff.component';

describe('CoordinatorStaffComponent', () => {
  let component: CoordinatorStaffComponent;
  let fixture: ComponentFixture<CoordinatorStaffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoordinatorStaffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoordinatorStaffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
