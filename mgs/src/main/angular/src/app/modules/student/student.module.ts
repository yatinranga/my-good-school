import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './home/home.component';
import { RouterModule, Routes } from '@angular/router';

import { StudentAwardsComponent } from './student-awards/student-awards.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { StudentComponent } from './student.component';
import { AddActivityComponent } from './student-activity/components/add-activity/add-activity.component';
import { ReviewActivityComponent } from './student-activity/components/review-activity/review-activity.component';
import { ActivityDraftsComponent } from './student-activity/components/activity-drafts/activity-drafts.component';
import { ProfileComponent } from './profile/profile.component';
import { StudentActivityModule } from './student-activity/student-activity.module';


const routes: Routes = [
  {
    path: ':id',
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
        children: [
          {
            path: 'add-activity',
            component: AddActivityComponent
          },
          {
            path: 'review-activity',
            component: ReviewActivityComponent
          },
          {
            path: 'activity-draft',
            component: ActivityDraftsComponent
          }
        ]
      },
      {
        path: 'awards-achievment',
        component: StudentAwardsComponent
      },
      {
        path: '',
        redirectTo: 'home'
      }
    ]
  },
  
]



@NgModule({
  declarations: [
    HomeComponent,
    StudentComponent,
    StudentAwardsComponent,
    SidebarComponent,
    ProfileComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    StudentActivityModule,
    RouterModule.forChild(routes),
  ],
  providers: []
})
export class StudentModule { }
