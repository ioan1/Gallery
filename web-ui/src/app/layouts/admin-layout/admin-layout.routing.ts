import { Routes } from '@angular/router';

import { DashboardComponent } from '../../components/dashboard/dashboard.component';
import { UserProfileComponent } from '../../user-profile/user-profile.component';
import { CategoryComponent } from '../../components/category/category.component';
import { AlbumComponent } from '../../components/album/album.component';
import { TypographyComponent } from '../../typography/typography.component';
import { IconsComponent } from '../../icons/icons.component';
import { MapsComponent } from '../../maps/maps.component';
import { NotificationsComponent } from '../../notifications/notifications.component';

export const AdminLayoutRoutes: Routes = [
    { path: 'dashboard',                        component: DashboardComponent },
    { path: 'user-profile',                     component: UserProfileComponent },
    { path: 'category/:id',                     component: CategoryComponent },
    { path: 'category/:idcat/album/:idalbum',   component: AlbumComponent },
    { path: 'typography',                       component: TypographyComponent },
    { path: 'icons',                            component: IconsComponent },
    { path: 'maps',                             component: MapsComponent },
    { path: 'notifications',                    component: NotificationsComponent },
];
