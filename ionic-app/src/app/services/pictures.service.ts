import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PicturesService {

  constructor(private http: HttpClient) { }

  listPictures(category, album) {
    return this.http.get('https://gallery.redby.fr/rest/pictures/' + category + '/' + album);
  }

  deleteThumbnailsCache() {
    // TODO
    return this.http.delete('https://gallery.redby.fr/rest/pictures/thumbnailsCache').subscribe();
  }

  deleteExifData() {
    // TODO
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
