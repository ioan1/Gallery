import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardSizePerYearChartComponent } from './dashboard-size-per-year-chart.component';

describe('DashboardSizePerYearChartComponent', () => {
  let component: DashboardSizePerYearChartComponent;
  let fixture: ComponentFixture<DashboardSizePerYearChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardSizePerYearChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardSizePerYearChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
