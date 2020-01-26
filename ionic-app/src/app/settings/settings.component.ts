import { Component, OnInit } from '@angular/core';
import {PicturesService} from "../services/pictures.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {

  constructor(private picturesService : PicturesService) {}

  ngOnInit() {}

  deleteThumbnails() {
    this.picturesService.deleteThumbnailsCache();
  }

  deleteExifData() {
    this.picturesService.deleteExifData();
  }

}
