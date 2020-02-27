import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  
  studentDetailsArr = [];
  studentProfile : FormGroup;
  editForm = "Student Profile";

  constructor(private formBuilder:FormBuilder,private studentService : StudentService) { }

  ngOnInit() {
    this.studentService.getStudentInfo().subscribe((res) => {
      this.studentDetailsArr = res;      
    },
      (err) => console.log(err)
    );

    this.studentProfile = this.formBuilder.group({
      studentName : [''],
      studentGender : [''],
      studentDob : [''],
      studentEmail : [''],
      studentMob : [''],
      studentSubscriptionEndDate : [''],
      guardianName : [''],
      guardianEmail : [''],
      guardianMob : [''],
      guardianRelationship : ['']
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
