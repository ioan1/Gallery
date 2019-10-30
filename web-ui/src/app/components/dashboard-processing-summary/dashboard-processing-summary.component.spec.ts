import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardProcessingSummaryComponent } from './dashboard-processing-summary.component';

describe('DashboardDiskUsageSummaryComponent', () => {
  let component: DashboardProcessingSummaryComponent;
  let fixture: ComponentFixture<DashboardProcessingSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardProcessingSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardProcessingSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
