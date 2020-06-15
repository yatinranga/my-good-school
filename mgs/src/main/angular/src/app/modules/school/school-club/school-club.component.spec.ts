import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchoolClubComponent } from './school-club.component';

describe('SchoolClubComponent', () => {
  let component: SchoolClubComponent;
  let fixture: ComponentFixture<SchoolClubComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchoolClubComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchoolClubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
