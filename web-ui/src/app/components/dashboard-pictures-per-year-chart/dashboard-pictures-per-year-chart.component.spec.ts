import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPicturesPerYearChartComponent } from './dashboard-pictures-per-year-chart.component';

describe('DashboardSizePerYearChartComponent', () => {
  let component: DashboardPicturesPerYearChartComponent;
  let fixture: ComponentFixture<DashboardPicturesPerYearChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardPicturesPerYearChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardPicturesPerYearChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
