import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-saved-actitvity',
  templateUrl: './saved-actitvity.component.html',
  styleUrls: ['./saved-actitvity.component.scss']
})
export class SavedActitvityComponent implements OnInit {

  activites = [];
  teachers = [];
  savedActivityForm : FormGroup;

  constructor( private formBuilder : FormBuilder ) { }

  ngOnInit() {
    this.savedActivityForm = this.formBuilder.group({
      savedActivity : [''],
      savedActivityDetails :[''],
      savedTeacher : [''],
      attachment : ['']
    })
  }

  submitActivity(){
    console.log(this.savedActivityForm.value);
  }

}
