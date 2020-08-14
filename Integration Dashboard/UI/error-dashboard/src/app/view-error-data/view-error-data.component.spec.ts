import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewErrorDataComponent } from './view-error-data.component';

describe('ViewErrorDataComponent', () => {
  let component: ViewErrorDataComponent;
  let fixture: ComponentFixture<ViewErrorDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewErrorDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewErrorDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
