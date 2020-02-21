import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-student-activity',
  templateUrl: './student-activity.component.html',
  styleUrls: ['./student-activity.component.scss']
})
export class StudentActivityComponent implements OnInit {

  activityType = false ;
  showAddActivityPopup: boolean;
  addActivityForm : FormGroup;

  activites = ["Yoga","Badminton","Judo"];
  teachers = ["Mr.Rakesh","Mrs.Seema"];  

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.addActivityForm = this.formBuilder.group({
      activity : [''],
      activityDetails :[''],
      teacher : [''],
      attachment : ['']
    })
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.addActivityForm.value['attachment'] = file;
      console.log(this.addActivityForm.value[''])
    }
  }

  saveActivity(){
    console.log(this.addActivityForm.value);
  }

  changeActivityType(str) {
    return this.activityType = !this.activityType;
  }
}
