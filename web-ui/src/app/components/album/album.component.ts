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
  picturesLoaded: number; // defined the number of pictures really loaded. When this = total number, then the gallery is loaded.

  public masonryOptions: NgxMasonryOptions = {
    transitionDuration: '0.5s',
    columnWidth: 300,
    gutter: 10
  };

  constructor(private route: ActivatedRoute, private picturesService: PicturesService) {
    this.picturesLoaded = 0;
  }

  ngOnInit() {
    this.route.paramMap.subscribe(pageParameters => {
      this.category = pageParameters.get('idcat');
      this.album = pageParameters.get('idalbum');
      // Fetch the pictures within this category
        this.picturesService.listPictures(this.category, this.album).subscribe((data: {}) => {
          this.pictures = data;
          console.log("Pictures retrieved and rendered");
      })
    });

  }

  /**
   * See: https://github.com/fengyuanchen/viewerjs/blob/master/README.md
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

  ngOnDestroy() {
    if (this.gallery != undefined) {
      console.log('Destroying the gallery');
      this.gallery.destroy();
    }
  }

  /**
   * Even triggered each time a thumbnail is really loaded.
   * This way, we start the gallery only when all images are loaded. This is important.
   * @param evt picture load event.
   */
  onImageLoad(evt) {
    if (evt && evt.target) {
      this.picturesLoaded++;
      if (this.picturesLoaded === this.pictures.length) {
        this.startGallery();
      }
    }
  }

}
