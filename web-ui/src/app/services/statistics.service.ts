import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  constructor(private http: HttpClient) { }

  getDiskUsage() {
    return this.http.get('https://gallery.redby.fr/rest/statistics/disk');
  }

  getWaitForProcessing() {
    return this.http.get('https://gallery.redby.fr/rest/statistics/waitForProcessing');
  }

  getSizePerYear() {
    return this.http.get('https://gallery.redby.fr/rest/statistics/sizePerYear');
  }

  getPicturesPerYear() {
    return this.http.get('https://gallery.redby.fr/rest/statistics/picturesPerYear');
  }

  getCachedThumbnails() {
    return this.http.get('https://gallery.redby.fr/rest/statistics/cachedThumbnails');
  }
}
