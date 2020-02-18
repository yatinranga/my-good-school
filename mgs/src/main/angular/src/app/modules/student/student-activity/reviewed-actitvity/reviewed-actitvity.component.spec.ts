import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewedActitvityComponent } from './reviewed-actitvity.component';

describe('ReviewedActitvityComponent', () => {
  let component: ReviewedActitvityComponent;
  let fixture: ComponentFixture<ReviewedActitvityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewedActitvityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewedActitvityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
