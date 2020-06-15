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
import { StudentListComponent } from './student-list/student-list.component';
import { StaffListComponent } from './staff-list/staff-list.component';
import { StudentDetailsComponent } from './student-list/student-details/student-details.component';
import { StaffDetailsComponent } from './staff-list/staff-details/staff-details.component';
import { SchoolClubComponent } from './school-club/school-club.component';
import { ClubDetailsComponent } from './school-club/club-details/club-details.component';
import { SupervisorDetailsComponent } from './school-club/supervisor-details/supervisor-details.component';

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
        path: 'student-list',
        component: StudentListComponent
      },
      {
        path: 'staff-list',
        component: StaffListComponent
      },
      {
        path: 'add-user',
        component: AddUserComponent
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
        path: 'club-society',
        component: SchoolClubComponent
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
    UsersListComponent, StudentListComponent, StaffListComponent, StudentDetailsComponent, StaffDetailsComponent, SchoolClubComponent, ClubDetailsComponent, SupervisorDetailsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
})
export class SchoolModule { }
