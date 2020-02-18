import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-add-activity',
  templateUrl: './add-activity.component.html',
  styleUrls: ['./add-activity.component.scss']
})
export class AddActivityComponent implements OnInit {

  activites = ["Yoga","Badminton","Judo"];
  teachers = ["Mr.Rakesh","Mrs.Seema"];
  addActivityForm : FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.addActivityForm = this.formBuilder.group({
      activity : [''],
      activityDetails :[''],
      teacher : [''],
      attachment : ['']
    })
  }

  addActivity(){
    console.log(this.addActivityForm.value);
  }

  // getActivity(event){
  //   console.log(event);
  // }

  // getTeacher(event){
  //   console.log(event);
  // }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.addActivityForm.value['attachment'] = file;
    }
  }

  saveActivity(){
    console.log("Activitiy Saved");
  }



}
