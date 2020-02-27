import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { AuthGuard } from './services/auth.guard';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'admin',
    loadChildren: 'src/app/modules/admin/admin.module#AdminModule',
    canActivate: [AuthGuard]
  },
  
  {
    path: 'signup',
    loadChildren :'src/app/modules/signup/signup.module#SignupModule'
    
  },
  {
    path: 'student',
    loadChildren: 'src/app/modules/student/student.module#StudentModule',
  },
  {
    path: 'teacher',
    loadChildren: 'src/app/modules/teacher/teacher.module#TeacherModule',
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  // {
  //   path: '**',
  //   redirectTo: '/login'
  // }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }