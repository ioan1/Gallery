import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {AlbumsService} from "../../services/albums.service";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  category  : string;
  albums    : any;
  constructor(private route: ActivatedRoute,
              public albumsService: AlbumsService ) { }

  ngOnInit() {

    this.route.paramMap.subscribe(pageParameters => {
      this.category = pageParameters.get('id');
      // Fetch the albums within this category
      this.albumsService.getAlbums(this.category).subscribe((data: {}) => {
        this.albums = data;
        console.log(data);
      })
    });

  }

}
