import { Component, OnInit } from '@angular/core';
import {StatisticsService} from "../../services/statistics.service";

@Component({
  selector: '[app-dashboard-thumbnails-cache]',
  templateUrl: './dashboard-thumbnails-cache.component.html',
  styleUrls: ['./dashboard-thumbnails-cache.component.scss']
})
export class DashboardThumbnailsCacheComponent implements OnInit {

  private cachedThumbnails: {};

  constructor(private statisticsService: StatisticsService) { }

  ngOnInit() {
    this.statisticsService.getCachedThumbnails().subscribe((data: {}) => {
      this.cachedThumbnails = data;
      console.log("Number of cached thumbnails: " + data);
    })
  }

}
