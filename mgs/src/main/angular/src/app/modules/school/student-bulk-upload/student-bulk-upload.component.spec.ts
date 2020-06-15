import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentBulkUploadComponent } from './student-bulk-upload.component';

describe('StudentBulkUploadComponent', () => {
  let component: StudentBulkUploadComponent;
  let fixture: ComponentFixture<StudentBulkUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentBulkUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentBulkUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
