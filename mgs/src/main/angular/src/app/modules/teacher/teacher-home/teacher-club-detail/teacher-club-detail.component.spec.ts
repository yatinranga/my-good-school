import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherClubDetailComponent } from './teacher-club-detail.component';

describe('TeacherClubDetailComponent', () => {
  let component: TeacherClubDetailComponent;
  let fixture: ComponentFixture<TeacherClubDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeacherClubDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeacherClubDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
