import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from "@angular/forms";
import {IonicModule} from "@ionic/angular";
import {RouterModule} from "@angular/router";
import {AlbumListComponent} from "./album-list/album-list.component";
import {AlbumDetailsComponent} from "./album-details/album-details.component";

@NgModule({
  declarations: [AlbumListComponent, AlbumDetailsComponent],
  imports: [
    CommonModule,
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild([
      {
        path: ':category',
        component: AlbumListComponent
      },
      {
        path: ':category/:albumId',
        component: AlbumDetailsComponent
      }
    ])
  ]
})
export class AlbumModule { }
