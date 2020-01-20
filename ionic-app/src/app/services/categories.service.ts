import {Injectable, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  private categories: Category[] = [];

  constructor(private http: HttpClient) {
    this.http.get('https://gallery.redby.fr/rest/categories').subscribe((x: []) => {
      x.forEach(value => {
        this.categories.push(value as any as Category);
      });
    });
  }

  public getCategories(): Category[] {
    return this.categories;
  }

}

export class Category {
  public name: string;
}
