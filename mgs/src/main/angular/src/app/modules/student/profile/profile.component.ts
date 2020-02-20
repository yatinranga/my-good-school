import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  studentProfile : FormGroup;
  editForm = "Student Profile";

  constructor(private formBuilder:FormBuilder) { }

  ngOnInit() {
    this.studentProfile = this.formBuilder.group({
      studentName : ['Ram'],
      studentGender : ['Male'],
      studentDob : ['04-11-1997'],
      studentEmail : ['ram@gmail.com'],
      studentMob : ['9999999999'],
      studentSubscriptionEndDate : ['01-12-2020'],
      guardianName : ['Ram Father'],
      guardianEmail : ['ram.father@gmail.com'],
      guardianMob : ['963852741'],
      guardianRelationship : ['Father']
    });
    // this.student 
  }

  setEditForm(value){
    this.editForm = value;
    console.log(this.editForm);
    // return this.studentProfile.controls['studentName'].patchValue('ghv')
  }

  cancelEditForm(){
    this.editForm = "Student Profile";
  }

  updateProfile(){
    this.studentProfile.patchValue;
    console.log(this.studentProfile);
  }

}
