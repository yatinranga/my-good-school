import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, Routes, RouterModule } from '@angular/router';
import { SignupComponent } from './signup.component';
import { StudentSignupComponent } from './student-signup/student-signup.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { TeacherSignupComponent } from './teacher-signup/teacher-signup.component';
import { StudentService } from 'src/app/services/student.service';
import { SchoolSignupComponent } from './school-signup/school-signup.component';

const routes: Routes = [
  {
    path: '',
    component: SignupComponent,
    children: [
      { path: 'student', component: StudentSignupComponent },
      { path: 'teacher', component: TeacherSignupComponent },
      { path: 'school', component: SchoolSignupComponent },
      { path: '', redirectTo: 'student' }
    ]
  }
]

@NgModule({
  declarations: [SignupComponent, StudentSignupComponent, TeacherSignupComponent, SchoolSignupComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)    
  ],
  providers: [StudentService]
})
export class SignupModule { }
