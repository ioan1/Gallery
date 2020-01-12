import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  constructor(private http: HttpClient) { }

  getDiskUsage() {
    return this.http.get(environment.service.statistics.url + '/disk');
  }

  getWaitForProcessing() {
    return this.http.get(environment.service.statistics.url + '/waitForProcessing');
  }

  getSizePerYear() {
    return this.http.get(environment.service.statistics.url + '/sizePerYear');
  }

  getPicturesPerYear() {
    return this.http.get(environment.service.statistics.url + '/picturesPerYear');
  }

  getCachedThumbnails() {
    return this.http.get(environment.service.statistics.url + '/cachedThumbnails');
  }
}
