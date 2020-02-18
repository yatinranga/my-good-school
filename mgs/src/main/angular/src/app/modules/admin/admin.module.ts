import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './admin.component';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { StudentUploadComponent } from './student-upload/student-upload.component';
import { TeacherUploadComponent } from './teacher-upload/teacher-upload.component';


const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      { path: 'student-upload', component: StudentUploadComponent },
      { path: 'teacher-upload', component: TeacherUploadComponent },
      { path: '', redirectTo : 'student-upload' }
    ]
  }
]

@NgModule({
  declarations: [AdminComponent, StudentUploadComponent, TeacherUploadComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})

export class AdminModule { }
