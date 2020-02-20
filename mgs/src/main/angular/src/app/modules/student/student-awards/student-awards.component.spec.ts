import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentAwardsComponent } from './student-awards.component';

describe('StudentAwardsComponent', () => {
  let component: StudentAwardsComponent;
  let fixture: ComponentFixture<StudentAwardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentAwardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentAwardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
