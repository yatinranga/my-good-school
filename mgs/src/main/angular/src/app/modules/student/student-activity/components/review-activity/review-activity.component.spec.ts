import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewActivityComponent } from './review-activity.component';

describe('ReviewActivityComponent', () => {
  let component: ReviewActivityComponent;
  let fixture: ComponentFixture<ReviewActivityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewActivityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
