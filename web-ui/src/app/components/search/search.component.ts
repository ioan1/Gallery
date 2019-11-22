import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AlbumsService} from "../../services/albums.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  keyword: string;
  albums: any;

  constructor(private route: ActivatedRoute,
              public albumsService: AlbumsService) { }

  ngOnInit() {

    // Search for albums
    this.route.paramMap.subscribe(pageParameters => {
      this.keyword = pageParameters.get('keyword');
      // Fetch the albums within this category
      this.albumsService.searchAlbums(this.keyword).subscribe((data: {}) => {
        this.albums = data;
        console.log("Got albums search results: " + data);
      })
    });

  }

}
