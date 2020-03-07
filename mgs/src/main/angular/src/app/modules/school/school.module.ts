import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SchoolComponent } from './school.component';
import { SchoolProfileComponent } from './school-profile/school-profile.component';
import { SchoolAwardsComponent } from './school-awards/school-awards.component';



@NgModule({
  declarations: [SchoolComponent, SchoolProfileComponent, SchoolAwardsComponent],
  imports: [
    CommonModule
  ]
})
export class SchoolModule { }
