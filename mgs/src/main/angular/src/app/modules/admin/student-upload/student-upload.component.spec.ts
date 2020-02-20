import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentUploadComponent } from './student-upload.component';

describe('StudentUploadComponent', () => {
  let component: StudentUploadComponent;
  let fixture: ComponentFixture<StudentUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
