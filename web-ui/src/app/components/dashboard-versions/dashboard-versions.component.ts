import { Component, OnInit } from '@angular/core';

@Component({
  selector: '[app-dashboard-versions]',
  templateUrl: './dashboard-versions.component.html',
  styleUrls: ['./dashboard-versions.component.scss']
})
export class DashboardVersionsComponent implements OnInit {

  versions: ActiveComponentVersionDetails [] = [];

  constructor() {
    this.versions.push({
      id: "service-albums",
      version: "1.0.0-SNAPSHOT",
      buildTimestamp: "25/12/2019 23:12:34",
      uptime: "25 days"
    });
  }

  ngOnInit() {
  }

}

export class ActiveComponentVersionDetails {
  id: string;
  version: string;
  buildTimestamp: string;
  uptime: string;
}
