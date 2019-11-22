import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardThumbnailsCacheComponent } from './dashboard-thumbnails-cache.component';

describe('DashboardThumbnailCacheComponent', () => {
  let component: DashboardThumbnailsCacheComponent;
  let fixture: ComponentFixture<DashboardThumbnailsCacheComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardThumbnailsCacheComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardThumbnailsCacheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
