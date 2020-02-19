import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-saved-actitvity',
  templateUrl: './saved-actitvity.component.html',
  styleUrls: ['./saved-actitvity.component.scss']
})
export class SavedActitvityComponent implements OnInit {

  activites = ["Yoga", "Badminton", "Judo"];
  teachers = ["Mr.Rakesh", "Mrs.Seema"];
  savedActivitiesArr = ["Yoga", "Badminton", "Cricket"];
  showActivites = false;
  savedActivityForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.savedActivityForm = this.formBuilder.group({
      savedActivity: [''],
      savedActivityDetails: [''],
      savedActivityDate: [''],
      savedTeacher: [''],
      attachment: ['']
    })
  }

  showSavedActivities() {
    this.showActivites = true;
  }

  showSelectedActivities(index){
    console.log(this.savedActivitiesArr[index]);
  }

  submitActivity() {
    console.log(this.savedActivityForm.value);
  }

}