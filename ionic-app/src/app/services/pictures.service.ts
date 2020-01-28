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

  deleteThumbnailsCache() {
    return this.http.delete('https://gallery.redby.fr/rest/pictures/thumbnailsCache').subscribe();
  }

  deleteExifData() {
    return this.http.delete('https://gallery.redby.fr/rest/exif').subscribe();
  }

}

export class Picture {

  category: string;
  name: string;
  album: string;
  file: string;
  path: string;
  exifData: PictureExifData;

}

/**
 * There are actually more than that, but for now these are the only ones used.
 */
export class PictureExifData {
  width: number;
  height: number;
  size: number;
}
