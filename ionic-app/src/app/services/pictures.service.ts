import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PicturesService {

  constructor(private http: HttpClient) { }

  listPictures(category, album) {
    console.log("Listing pictures from category="+category+" and album="+album);
    return this.http.get('https://gallery.redby.fr/rest/pictures/' + category + '/' + album);
  }

}
