import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from "@angular/forms";
import {IonicModule} from "@ionic/angular";
import {RouterModule} from "@angular/router";
import {AlbumListComponent} from "./album-list/album-list.component";

@NgModule({
  declarations: [AlbumListComponent],
  imports: [
    CommonModule,
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild([
      {
        path: ':id',
        component: AlbumListComponent
      }
    ])
  ]
})
export class AlbumModule { }
