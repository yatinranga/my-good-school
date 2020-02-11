import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path:'',
        redirectTo:'home'
      },
      {
        path:'home',
        component:HomeComponent
      }
    ])
  ]
})
export class StudentModule { }
