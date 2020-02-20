import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherUploadComponent } from './teacher-upload.component';

describe('TeacherUploadComponent', () => {
  let component: TeacherUploadComponent;
  let fixture: ComponentFixture<TeacherUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeacherUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeacherUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
