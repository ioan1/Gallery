import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class AlbumsService {

    constructor(private http: HttpClient) { }

    public getAlbums(category): Observable<Album[]> {
        return this.http.get<Album[]>('https://gallery.redby.fr/rest/albums/list/' + category);
    }

    public searchAlbums(keyword: string): Observable<Album[]> {
        return this.http.get<Album[]>('https://gallery.redby.fr/rest/albums/search/' + keyword);
    }
}

export class Album {
    date: Date;
    category: Number;
    name: string;
    id: string;
    path: string;
    files: Number;
    pictures: Number;
    videos: Number;
    others: Number;
}
