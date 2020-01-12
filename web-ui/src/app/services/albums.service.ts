import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from 'environments/environment';


@Injectable({
    providedIn: 'root'
})
export class AlbumsService {

    constructor(private http: HttpClient) {
    }

    getAlbums(category) {
        return this.http.get(environment.service.albums.url + '/list/' + category);
    }

    searchAlbums(keyword: string) {
        return this.http.get(environment.service.albums.url + '/search/' + keyword);
    }
}
