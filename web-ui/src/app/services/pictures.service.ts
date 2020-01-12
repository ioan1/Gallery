import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PicturesService {

  constructor(private http: HttpClient) { }

  listPictures(category, album) {
    console.log("Listing pictures from category="+category+" and album="+album);
    return this.http.get(environment.service.pictures.url + '/' + category + '/' + album);
  }

}
