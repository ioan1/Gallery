import { Component, OnInit } from '@angular/core';
import {CategoriesService} from '../../services/categories.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit {

  appPages = [
    {
      title: 'Home',
      url: '/home',
      icon: 'home'
    }
  ];

  constructor(private categoriesService : CategoriesService) { }

  ngOnInit() {
    console.log('on init menu');
    this.categoriesService.getCategories().forEach(value => {
      console.log(value);
      this.appPages.push({
        title: value.name,
        url: '/category/' + value.name,
        icon: 'list'
      });
    });
  }

}
