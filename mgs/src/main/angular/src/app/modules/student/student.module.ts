import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { SidebarComponent } from './sidebar/sidebar.component';
import { StudentComponent } from './student.component';

import { HomeComponent } from './home/home.component';
import { StudentAwardsComponent } from './student-awards/student-awards.component';
import { ProfileComponent } from './profile/profile.component';
import { SavedActitvityComponent } from './student-activity/saved-actitvity/saved-actitvity.component';
import { StudentActivityComponent } from './student-activity/student-activity.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { StudentClubDetailsComponent } from './home/student-club-details/student-club-details.component';

const routes: Routes = [
  {
    path: '',
    component: StudentComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path: 'profile',
        component: ProfileComponent
      },
      {
        path: 'activity',
        component: StudentActivityComponent,
        children: [
          {
            path: '',
            component: SavedActitvityComponent
          }
        ]
      },
      {
        path: 'awards-achievement',
        component: StudentAwardsComponent
      },
      {
        path: 'certificates',
        component: CertificatesComponent
      },
      {
        path: '',
        redirectTo: 'home'
      }
    ],
  },
  {
    path: 'club/:name',
    component: StudentClubDetailsComponent,
  },
]

@NgModule({
  declarations: [
    HomeComponent,
    StudentComponent,
    StudentAwardsComponent,
    SidebarComponent,
    SavedActitvityComponent,
    StudentActivityComponent,
    ProfileComponent,
    CertificatesComponent,
    StudentClubDetailsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
  providers: []
})
export class StudentModule { }
