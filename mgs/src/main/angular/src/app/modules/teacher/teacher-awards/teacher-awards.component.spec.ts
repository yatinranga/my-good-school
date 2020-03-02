import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherAwardsComponent } from './teacher-awards.component';

describe('TeacherAwardsComponent', () => {
  let component: TeacherAwardsComponent;
  let fixture: ComponentFixture<TeacherAwardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeacherAwardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeacherAwardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
