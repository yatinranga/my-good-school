import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, Routes, RouterModule } from '@angular/router';
import { SignupComponent } from './signup.component';
import { StudentSignupComponent } from './student-signup/student-signup.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TeacherSignupComponent } from './teacher-signup/teacher-signup.component';
import { StudentService } from 'src/app/services/student.service';

const routes: Routes = [
  {
    path: '',
    component: SignupComponent,
    children: [
      { path: 'student', component: StudentSignupComponent },
      { path: 'teacher', component: TeacherSignupComponent },
      { path: '', redirectTo: 'student' }
    ]
  }
]

@NgModule({
  declarations: [SignupComponent, StudentSignupComponent, TeacherSignupComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)    
  ],
  providers: [StudentService]
})
export class SignupModule { }
