import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivityDraftsComponent } from './activity-drafts.component';

describe('ActivityDraftsComponent', () => {
  let component: ActivityDraftsComponent;
  let fixture: ComponentFixture<ActivityDraftsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActivityDraftsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivityDraftsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
