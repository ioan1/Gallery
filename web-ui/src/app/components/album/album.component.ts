import {Component, OnInit } from '@angular/core';
import {ActivatedRoute } from '@angular/router';
import {PicturesService} from "../../services/pictures.service";
import {NgxMasonryOptions} from "ngx-masonry";
import Viewer from "viewerjs";

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
  gallery   : any;

  public masonryOptions: NgxMasonryOptions = {
    transitionDuration: '0.5s',
    columnWidth: 300,
    gutter: 10
  };

  constructor(private route: ActivatedRoute, private picturesService: PicturesService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(pageParameters => {
      this.category = pageParameters.get('idcat');
      this.album = pageParameters.get('idalbum');
      // Fetch the pictures within this category
        this.picturesService.listPictures(this.category, this.album).subscribe((data: {}) => {
          // render thumbnails
          this.pictures = data;
          console.log(data);
          setTimeout(this.startGallery, 500); // Obliged to have a small delay when starting the gallery, letting time maybe for the DOM to be there ...
      })
    });

  }

  /**
   * See: https://github.com/fengyuanchen/viewerjs/blob/master/README.md
   * TODO: ensure this is called only when all thumbnails are loaded !!
   */
  startGallery() {
    this.gallery = new Viewer(document.getElementById('pictures'), {
      url(image) {
        return image.src.replace('/small/', '/full/');
      },
    });
    console.log('Starting the gallery');
    this.gallery.full();
  }

  public ngOnDestroy() {
    if (this.gallery != undefined) {
      console.log('Destroying the gallery');
      this.gallery.destroy();
    }
  }

}
