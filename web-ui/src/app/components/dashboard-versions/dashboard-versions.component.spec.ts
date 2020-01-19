import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardVersionsComponent } from './dashboard-versions.component';

describe('DashboardVersionsComponent', () => {
  let component: DashboardVersionsComponent;
  let fixture: ComponentFixture<DashboardVersionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardVersionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardVersionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
