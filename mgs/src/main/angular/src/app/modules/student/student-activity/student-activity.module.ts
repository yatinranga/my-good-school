import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddActivityComponent } from './components/add-activity/add-activity.component';
import { ReviewActivityComponent } from './components/review-activity/review-activity.component';
import { ActivityDraftsComponent } from './components/activity-drafts/activity-drafts.component';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [AddActivityComponent, ReviewActivityComponent, ActivityDraftsComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [
    AddActivityComponent,
    ReviewActivityComponent,
    ActivityDraftsComponent
  ]
})
export class StudentActivityModule { }
