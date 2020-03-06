import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchoolUploadComponent } from './school-upload.component';

describe('SchoolUploadComponent', () => {
  let component: SchoolUploadComponent;
  let fixture: ComponentFixture<SchoolUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchoolUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchoolUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
