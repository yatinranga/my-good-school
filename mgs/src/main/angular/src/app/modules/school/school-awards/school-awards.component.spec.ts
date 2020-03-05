import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchoolAwardsComponent } from './school-awards.component';

describe('SchoolAwardsComponent', () => {
  let component: SchoolAwardsComponent;
  let fixture: ComponentFixture<SchoolAwardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchoolAwardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchoolAwardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
