import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';


import { TeacherComponent } from './teacher.component';
import { TeacherHomeComponent } from './teacher-home/teacher-home.component';
import { TeacherProfileComponent } from './teacher-profile/teacher-profile.component';
import { TeacherSidebarComponent } from './teacher-sidebar/teacher-sidebar.component';
import { TeacherActivityComponent } from './teacher-activity/teacher-activity.component';
import { TeacherAwardsComponent } from './teacher-awards/teacher-awards.component';

const routes: Routes = [
  {
    path: ':id',
    component: TeacherComponent,
    children: [
      {
        path: 'home',
        component: TeacherHomeComponent
      },
      {
        path: 'profile',
        component: TeacherProfileComponent
      },
      {
        path: 'activity',
        component: TeacherActivityComponent
      },
      {
        path: 'awards-achievment',
        component: TeacherAwardsComponent
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
    TeacherHomeComponent, 
    TeacherProfileComponent, 
    TeacherSidebarComponent,
    TeacherComponent,
    TeacherActivityComponent,
    TeacherAwardsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
})

export class TeacherModule { }
