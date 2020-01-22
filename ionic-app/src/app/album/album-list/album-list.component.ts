import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Album, AlbumsService} from "../../services/albums.service";

@Component({
  selector: 'app-album-list',
  templateUrl: './album-list.component.html',
  styleUrls: ['./album-list.component.scss'],
})
export class AlbumListComponent implements OnInit {

  category  : Number;
  albums    : Album [];

  constructor(private route: ActivatedRoute,
              public albumsService: AlbumsService ) { }

  ngOnInit() {
    this.route.paramMap.subscribe(pageParameters => {
      this.category = Number.parseInt(pageParameters.get('category'));
      // Fetch the albums within this category
      this.albumsService.getAlbums(this.category).subscribe((data: Album[]) => {
        this.albums = data;
        console.log(data);
      })
    });
  }

}
