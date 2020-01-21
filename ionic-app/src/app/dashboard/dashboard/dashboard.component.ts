import { Component, OnInit } from '@angular/core';
import {DiskUsage, StatisticsService} from "../../services/statistics.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {

  diskUsage: DiskUsage;
  waitForProcessing: Number;
  cachedThumbnails: Number;

  constructor(private statisticsService: StatisticsService) { }

  ngOnInit() {
    /** Disk usage **/
    this.statisticsService.getDiskUsage().subscribe((data: DiskUsage) => {
      this.diskUsage = data;
    });
    /** Files wait for processing **/
    this.statisticsService.getWaitForProcessing().subscribe((data: Number) => {
      this.waitForProcessing = data;
    });
    /** Thumbnails that are cached via redis. **/
    this.statisticsService.getCachedThumbnails().subscribe((data: Number) => {
      this.cachedThumbnails = data;
    })
  }

}
