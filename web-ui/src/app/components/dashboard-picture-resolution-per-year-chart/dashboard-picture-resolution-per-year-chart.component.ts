import { Component, OnInit } from '@angular/core';
import {StatisticsService} from "../../services/statistics.service";
import * as Chartist from "chartist";
import * as moment from 'moment';

@Component({
  selector: '[app-dashboard-picture-resolution-per-year-chart]',
  templateUrl: './dashboard-picture-resolution-per-year-chart.component.html',
  styleUrls: ['./dashboard-picture-resolution-per-year-chart.component.scss']
})
export class DashboardPictureResolutionPerYearChartComponent implements OnInit {

  private pictureResolutionPerYear: {};

  constructor(private statisticsService: StatisticsService) { }

  ngOnInit() {
    /** Initial state of some widgets. */
    jQuery("#comp-pictureResolutionPerYearChart").hide();

    // Fetch statistics about the picture resolution per year (year => average MP used)
    this.statisticsService.getPictureResolutionPerYear().subscribe((data: {}) => {
      this.pictureResolutionPerYear = data;
      console.log(data);
      jQuery("#comp-pictureResolutionPerYearChart").show(250);
      var pictureResolutionPerYearChart = new Chartist.Line('#pictureResolutionPerYearChart', {
        series: [ this.pictureResolutionPerYear ]
      }, {
        axisX: {
          type: Chartist.FixedScaleAxis,
          divisor: 5,
          labelInterpolationFnc: function(value) {
            return moment(value).format('YYYY');
          }
        }
      });
      this.startAnimationForLineChart(pictureResolutionPerYearChart);
    })
  }

  startAnimationForLineChart(chart){
    let seq: any, delays: any, durations: any;
    seq = 0;
    delays = 80;
    durations = 500;

    chart.on('draw', function(data) {
      if(data.type === 'line' || data.type === 'area') {
        data.element.animate({
          d: {
            begin: 600,
            dur: 700,
            from: data.path.clone().scale(1, 0).translate(0, data.chartRect.height()).stringify(),
            to: data.path.clone().stringify(),
            easing: Chartist.Svg.Easing.easeOutQuint
          }
        });
      } else if(data.type === 'point') {
        seq++;
        data.element.animate({
          opacity: {
            begin: seq * delays,
            dur: durations,
            from: 0,
            to: 1,
            easing: 'ease'
          }
        });
      }
    });

    seq = 0;
  };

}
