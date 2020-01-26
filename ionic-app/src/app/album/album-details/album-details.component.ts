import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Picture, PicturesService} from "../../services/pictures.service";
import * as PhotoSwipe from "photoswipe";
import * as PhotoSwipeUI_Default from "photoswipe/dist/photoswipe-ui-default";

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
        var items = [
            {
                src: 'https://placekitten.com/600/400',
                w: 600,
                h: 400
            },
            {
                src: 'https://placekitten.com/1200/900',
                w: 1200,
                h: 900
            }
        ];

        // define options (if needed)
        var options = {
            // optionName: 'option value'
            // for example:
            index: 0 // start at first slide
        };

        // Initializes and opens PhotoSwipe
        var gallery = new PhotoSwipe(pswpElement as HTMLElement, PhotoSwipeUI_Default, items, options);
        gallery.init();
    }

}
