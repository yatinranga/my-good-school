import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { SchoolComponent } from './school.component';
import { SchoolProfileComponent } from './school-profile/school-profile.component';
import { SchoolAwardsComponent } from './school-awards/school-awards.component';
import { SchoolHomeComponent } from './school-home/school-home.component';

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
    SchoolHomeComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
})
export class SchoolModule { }
