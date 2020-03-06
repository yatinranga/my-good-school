import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchoolSignupComponent } from './school-signup.component';

describe('SchoolSignupComponent', () => {
  let component: SchoolSignupComponent;
  let fixture: ComponentFixture<SchoolSignupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchoolSignupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchoolSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
