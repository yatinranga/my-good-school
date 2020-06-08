import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AdminComponent } from './admin.component';
import { StudentUploadComponent } from './student-upload/student-upload.component';
import { TeacherUploadComponent } from './teacher-upload/teacher-upload.component';
import { SchoolUploadComponent } from './school-upload/school-upload.component';
import { UsersComponent } from './users/users.component';
import { AddUserComponent } from './add-user/add-user.component';
import { CreateRoleComponent } from './create-role/create-role.component';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      { path: 'student-upload', component: StudentUploadComponent },
      { path: 'supervisor-upload', component: TeacherUploadComponent },
      { path: 'school-upload', component: SchoolUploadComponent },
      { path: 'users', component: UsersComponent },
      { path: 'add-user', component: AddUserComponent },
      { path: 'create-role', component: CreateRoleComponent},

      { path: '', redirectTo : 'student-upload' }
    ]
  }
]

@NgModule({
  declarations: [AdminComponent, StudentUploadComponent, TeacherUploadComponent, SchoolUploadComponent, UsersComponent, AddUserComponent, CreateRoleComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})

export class AdminModule { }
