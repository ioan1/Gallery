import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PicturesService {

  constructor(private http: HttpClient) { }

  listPictures(category, album) {
    return this.http.get('https://gallery.redby.fr/rest/pictures/'+category.name+'/'+album);
  }

}
