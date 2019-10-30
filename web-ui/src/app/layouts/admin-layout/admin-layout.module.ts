import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdminLayoutRoutes } from './admin-layout.routing';
import { DashboardComponent } from '../../components/dashboard/dashboard.component';
import { UserProfileComponent } from '../../user-profile/user-profile.component';
import { CategoryComponent } from '../../components/category/category.component';
import { AlbumComponent } from '../../components/album/album.component';
import { TypographyComponent } from '../../typography/typography.component';
import { IconsComponent } from '../../icons/icons.component';
import { MapsComponent } from '../../maps/maps.component';
import { NotificationsComponent } from '../../notifications/notifications.component';
import { NgxMasonryModule } from 'ngx-masonry';
import {DashboardSizePerYearChartComponent} from "../../components/dashboard-size-per-year-chart/dashboard-size-per-year-chart.component";
import {DashboardDiskUsageSummaryComponent} from "../../components/dashboard-disk-usage-summary/dashboard-disk-usage-summary.component";
import {DashboardProcessingSummaryComponent} from "../../components/dashboard-processing-summary/dashboard-processing-summary.component";

import {
  MatButtonModule,
  MatInputModule,
  MatRippleModule,
  MatFormFieldModule,
  MatTooltipModule,
  MatSelectModule
} from '@angular/material';
@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatRippleModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTooltipModule,
    NgxMasonryModule
  ],
  declarations: [
    DashboardComponent,
    DashboardSizePerYearChartComponent,
    DashboardDiskUsageSummaryComponent,
    DashboardProcessingSummaryComponent,
    UserProfileComponent,
    CategoryComponent,
    AlbumComponent,
    TypographyComponent,
    IconsComponent,
    MapsComponent,
    NotificationsComponent,
  ]
})

export class AdminLayoutModule {}
