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
  results: any;

  constructor(private route: ActivatedRoute,
              public albumsService: AlbumsService) { }

  ngOnInit() {

    this.route.paramMap.subscribe(pageParameters => {
      this.keyword = pageParameters.get('keyword');
      // Fetch the albums within this category
      this.albumsService.searchAlbums(this.keyword).subscribe((data: {}) => {
        this.results = data;
        console.log("Got search results: " + data);
      })
    });

  }

}
