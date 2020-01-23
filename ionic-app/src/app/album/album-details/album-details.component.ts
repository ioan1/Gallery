import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Picture, PicturesService} from "../../services/pictures.service";

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html',
  styleUrls: ['./album-details.component.scss'],
})
export class AlbumDetailsComponent implements OnInit {

  year        : Number;
  albumId     : string;
  pictures    : Picture [];

  constructor(private route: ActivatedRoute,
              private picturesService: PicturesService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(pageParameters => {
      this.albumId = pageParameters.get('albumId');
      this.year = Number.parseInt(pageParameters.get('category'));
      // Fetch the pictures within this album
      this.picturesService.listPictures(this.year, this.albumId).subscribe((data: Picture[]) => {
        this.pictures = data;
      })
    });
  }

}
