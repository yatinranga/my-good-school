import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedActitvityComponent } from './saved-actitvity.component';

describe('SavedActitvityComponent', () => {
  let component: SavedActitvityComponent;
  let fixture: ComponentFixture<SavedActitvityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SavedActitvityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SavedActitvityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
