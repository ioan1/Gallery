import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DiskUsage} from "../models/disk-usage";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  constructor(private http: HttpClient) { }

  getDiskUsage() {
    return this.http.get('https://gallery.redby.fr/rest/statistics/disk');
  }

  getSizePerYear() {
    return this.http.get('https://gallery.redby.fr/rest/statistics/sizePerYear');
  }

}
