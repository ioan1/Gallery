import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardDiskUsageSummaryComponent } from './dashboard-disk-usage-summary.component';

describe('DashboardDiskUsageSummaryComponent', () => {
  let component: DashboardDiskUsageSummaryComponent;
  let fixture: ComponentFixture<DashboardDiskUsageSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardDiskUsageSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardDiskUsageSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
