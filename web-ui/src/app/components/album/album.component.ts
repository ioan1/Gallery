import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {PicturesService} from "../../services/pictures.service";

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.css']
})
export class AlbumComponent implements OnInit {

  category  : string;
  album     : string;
  pictures  : any;
  videos    : any; // TODO
  others    : any; // TODO

  constructor(private route: ActivatedRoute, private picturesService: PicturesService) { }

  ngOnInit() {

    this.route.paramMap.subscribe(pageParameters => {
      this.category = pageParameters.get('idcat');
      this.album = pageParameters.get('idalbum');
      // Fetch the pictures within this category
      this.picturesService.listPictures(this.category, this.album).subscribe((data: {}) => {
        this.pictures = data;
        console.log(data);
      })
    });

  }

}
