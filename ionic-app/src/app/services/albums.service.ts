import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class AlbumsService {

    constructor(private http: HttpClient) { }

    public getAlbums(category: Number): Observable<Album[]> {
        return this.http.get<Album[]>('https://gallery.redby.fr/rest/albums/list/' + category);
    }

    public searchAlbums(keyword: string): Observable<Album[]> {
        return this.http.get<Album[]>('https://gallery.redby.fr/rest/albums/search/' + keyword);
    }
}

export class Album {
    public date: Date;
    public category: Number;
    public name: string;
    public id: string;
    public path: string;
    public thumbnail: string;
    public files: Number;
    public pictures: Number;
    public videos: Number;
    public others: Number;
}
