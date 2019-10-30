import { Component, OnInit } from '@angular/core';
import {StatisticsService} from "../../services/statistics.service";

@Component({
  selector: '[app-dashboard-processing-summary]',
  templateUrl: './dashboard-processing-summary.component.html',
  styleUrls: ['./dashboard-processing-summary.component.scss']
})
export class DashboardProcessingSummaryComponent implements OnInit {

  private waitForProcessing: {};

  constructor(private statisticsService: StatisticsService) { }

  ngOnInit() {
    /** Disk usage **/
    this.statisticsService.getWaitForProcessing().subscribe((data: {}) => {
      this.waitForProcessing = data;
      console.log("Wait for processing: " + data);
    })
  }

}
