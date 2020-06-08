import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { SchoolComponent } from './school.component';
import { SchoolProfileComponent } from './school-profile/school-profile.component';
import { SchoolAwardsComponent } from './school-awards/school-awards.component';
import { SchoolHomeComponent } from './school-home/school-home.component';
import { AddUserComponent } from './add-user/add-user.component';
import { StudentBulkUploadComponent } from './student-bulk-upload/student-bulk-upload.component';
import { SupervisorBulkUploadComponent } from './supervisor-bulk-upload/supervisor-bulk-upload.component';
import { UsersListComponent } from './users-list/users-list.component';

const routes: Routes = [
  {
    path: '',
    component: SchoolComponent,
    children: [
      {
        path: 'home',
        component: SchoolHomeComponent
      },
      {
        path: 'profile',
        component: SchoolProfileComponent
      },
      {
        path: 'student-upload',
        component: StudentBulkUploadComponent
      },
      {
        path: 'supervisor-upload',
        component: SupervisorBulkUploadComponent
      },
      {
        path: 'users',
        component: UsersListComponent
      },
      {
        path: 'add-user',
        component: AddUserComponent
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
    SchoolComponent,
    SchoolProfileComponent,
    SchoolAwardsComponent,
    SchoolHomeComponent, 
    AddUserComponent, 
    StudentBulkUploadComponent, 
    SupervisorBulkUploadComponent, 
    UsersListComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
})
export class SchoolModule { }
