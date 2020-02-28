import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-student-activity',
  templateUrl: './student-activity.component.html',
  styleUrls: ['./student-activity.component.scss']
})
export class StudentActivityComponent implements OnInit {

  activityType = false ;
  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {}
 

  changeActivityType(str) {
    return this.activityType = !this.activityType;
  }
}
