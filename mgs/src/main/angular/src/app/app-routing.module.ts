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
    path: 'Admin',
    loadChildren: 'src/app/modules/admin/admin.module#AdminModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'signup',
    loadChildren: 'src/app/modules/signup/signup.module#SignupModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'Student',
    loadChildren: 'src/app/modules/student/student.module#StudentModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'Supervisor',
    loadChildren: 'src/app/modules/teacher/teacher.module#TeacherModule',
    canActivate: [AuthGuard]
  }, 
  {
    path: 'School',
    loadChildren: 'src/app/modules/school/school.module#SchoolModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'Coordinator',
    loadChildren: 'src/app/modules/coordinator/coordinator.module#CoordinatorModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'Head',
    loadChildren: 'src/app/modules/coordinator/coordinator.module#CoordinatorModule',
    canActivate: [AuthGuard]
  }, 
  // {
  //   path: '',
  //   redirectTo: ''+JSON.parse(localStorage.getItem('user_type')),
  //   pathMatch: 'full'
  // },
  // {
  //   path: '**',
  //   redirectTo: '/login'
  // }
];

@NgModule({
  imports: [RouterModule.forRoot(routes,{useHash:true})],
  exports: [RouterModule]
})

export class AppRoutingModule { }