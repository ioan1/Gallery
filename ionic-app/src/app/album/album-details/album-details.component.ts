import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Picture, PicturesService} from "../../services/pictures.service";
import * as PhotoSwipe from "photoswipe";
import * as PhotoSwipeUI_Default from "photoswipe/dist/photoswipe-ui-default";
import {formatNumber} from "@angular/common";

@Component({
    selector: 'app-album-details',
    templateUrl: './album-details.component.html',
    styleUrls: ['./album-details.component.scss'],
})
export class AlbumDetailsComponent implements OnInit {

    year: Number;
    albumId: string;
    pictures: Picture [];

    constructor(private route: ActivatedRoute,
                private picturesService: PicturesService) {
    }

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

    /**
     * Opens the picture in full screen mode.
     * @param pictureIndex
     */
    openGallery(pictureIndex: number) {
        var pswpElement = document.querySelectorAll('.pswp')[0];

        // build items array
        var items = [];
        this.pictures.forEach(picture => {
            items.push({
                src: 'https://gallery.redby.fr/rest/picture/medium/'+picture.path,
                w: picture.exifData.width,
                h: picture.exifData.height
            });
        });

        // define options (if needed)
        var options = {
            index: pictureIndex
        };

        // Initializes and opens PhotoSwipe
        var gallery = new PhotoSwipe(pswpElement as HTMLElement, PhotoSwipeUI_Default, items, options);
        gallery.init();
    }

}
