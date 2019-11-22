import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AlbumsService {

    constructor(private http: HttpClient) {
    }

    getAlbums(category) {
        return this.http.get('https://gallery.redby.fr/rest/albums/list/' + category);
    }

    searchAlbums(keyword: string) {
        return this.http.get('https://gallery.redby.fr/rest/albums/search/' + keyword);
    }
}
