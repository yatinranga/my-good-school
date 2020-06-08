import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SupervisorBulkUploadComponent } from './supervisor-bulk-upload.component';

describe('SupervisorBulkUploadComponent', () => {
  let component: SupervisorBulkUploadComponent;
  let fixture: ComponentFixture<SupervisorBulkUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SupervisorBulkUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SupervisorBulkUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
