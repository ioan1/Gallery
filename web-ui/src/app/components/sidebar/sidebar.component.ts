import { Component, OnInit } from '@angular/core';
import { CategoriesService } from "../../services/categories.service";

declare const $: any;
declare interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
}
export const ROUTES: RouteInfo[] = [
    { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: '' },
    { path: '/user-profile', title: 'User Profile',  icon:'person', class: '' }, // TODO
    { path: '/typography', title: 'Typography',  icon:'library_books', class: '' }, // TODO
    { path: '/icons', title: 'Icons',  icon:'bubble_chart', class: '' }, // TODO
    { path: '/maps', title: 'Maps',  icon:'location_on', class: '' }, // TODO
    { path: '/notifications', title: 'Notifications',  icon:'notifications', class: '' }, // TODO
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  menuItems: any[];
  categories: any;

  constructor(public categoriesService: CategoriesService ) { }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);

    // Fetch the years (categories) and add them to the menu items.
    this.categoriesService.getCategories().subscribe((data: {}) => {
        this.categories = data;
    })
  }
  isMobileMenu() {
      if ($(window).width() > 991) {
          return false;
      }
      return true;
  };
}
