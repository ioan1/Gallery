import { Component, OnInit } from '@angular/core';
import {StatisticsService} from "../../services/statistics.service";

@Component({
  selector: '[app-dashboard-disk-usage-summary]',
  templateUrl: './dashboard-disk-usage-summary.component.html',
  styleUrls: ['./dashboard-disk-usage-summary.component.scss']
})
export class DashboardDiskUsageSummaryComponent implements OnInit {

  private diskUsage: {};

  constructor(private statisticsService: StatisticsService) { }

  ngOnInit() {
    /** Disk usage **/
    this.statisticsService.getDiskUsage().subscribe((data: {}) => {
      this.diskUsage = data;
      console.log(data);
    })
  }

}
