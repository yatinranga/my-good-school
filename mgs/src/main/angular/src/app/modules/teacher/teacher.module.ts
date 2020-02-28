import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeacherHomeComponent } from './teacher-home/teacher-home.component';
import { TeacherProfileComponent } from './teacher-profile/teacher-profile.component';
import { Routes, RouterModule } from '@angular/router';
import { TeacherComponent } from './teacher.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TeacherSidebarComponent } from './teacher-sidebar/teacher-sidebar.component';

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
        path: '',
        redirectTo: 'home'
      }
    ]
  },
]


@NgModule({
  declarations: [TeacherHomeComponent, TeacherProfileComponent, TeacherSidebarComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
})

export class TeacherModule { }
