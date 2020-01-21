import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  constructor(private http: HttpClient) { }

  public getDiskUsage(): Observable<DiskUsage> {
    return this.http.get<DiskUsage>( 'https://gallery.redby.fr/rest/statistics/disk');
  }

  public getWaitForProcessing(): Observable<Number> {
    return this.http.get<Number>('https://gallery.redby.fr/rest/statistics/waitForProcessing');
  }

  public getCachedThumbnails(): Observable<Number>{
    return this.http.get<Number>('https://gallery.redby.fr/rest/statistics/cachedThumbnails');
  }
}

export class DiskUsage {
  used: Number;
  available: Number;
  unit: string;
}
