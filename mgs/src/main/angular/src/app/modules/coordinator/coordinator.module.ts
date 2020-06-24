import { NgModule, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { CoordinatorComponent } from './coordinator.component';
import { CoordinatorHomeComponent } from './coordinator-home/coordinator-home.component';
import { CoordinatorProfileComponent } from './coordinator-profile/coordinator-profile.component';
import { CoordinatorStudentsComponent } from './coordinator-students/coordinator-students.component';
import { CoordinatorStaffComponent } from './coordinator-staff/coordinator-staff.component';
import { CoordinatorAwardsComponent } from './coordinator-awards/coordinator-awards.component';
import { CoordinatorActivityComponent } from './coordinator-activity/coordinator-activity.component';

// search module
import { Ng2SearchPipeModule } from 'ng2-search-filter';

const routes: Routes = [
  {
    path: '',
    component: CoordinatorComponent,
    children: [
      {
        path: 'home',
        component: CoordinatorHomeComponent
      },
      {
        path: 'profile',
        component: CoordinatorProfileComponent
      },
      {
        path: 'awards',
        component: CoordinatorAwardsComponent
      },
      {
        path: 'activities',
        component: CoordinatorActivityComponent
      },
      {
        path: 'students',
        component: CoordinatorStudentsComponent
      },
      {
        path: 'supervisor',
        component: CoordinatorStaffComponent
      },
      // {
      //   path: '',
      //   redirectTo: 'home'
      // }
    ]
  }
]



@NgModule({
  declarations: [CoordinatorComponent, CoordinatorHomeComponent, CoordinatorProfileComponent, CoordinatorStudentsComponent, CoordinatorStaffComponent, CoordinatorAwardsComponent, CoordinatorActivityComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    Ng2SearchPipeModule

  ]
})
export class CoordinatorModule { }
