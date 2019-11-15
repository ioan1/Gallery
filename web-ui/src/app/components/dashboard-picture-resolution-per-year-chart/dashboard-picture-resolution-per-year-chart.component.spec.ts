import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPictureResolutionPerYearChartComponent } from './dashboard-picture-resolution-per-year-chart.component';

describe('DashboardSizePerYearChartComponent', () => {
  let component: DashboardPictureResolutionPerYearChartComponent;
  let fixture: ComponentFixture<DashboardPictureResolutionPerYearChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardPictureResolutionPerYearChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardPictureResolutionPerYearChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
