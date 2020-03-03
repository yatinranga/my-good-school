import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  studentProfile: FormGroup;
  editForm = 'Student Profile';
  studentDetails = {};
  studentInfo: any;
  studentId: any;
  tabs = 'guardianOne';

  constructor(private formBuilder: FormBuilder, private studentService: StudentService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.studentService.getProfile(this.studentId).subscribe((res) => {
      this.studentDetails = res;
      console.log(this.studentDetails);
    },
      (err) => console.log(err)
    );

    // this.studentProfile = this.formBuilder.group({
    //   studentName : [''],
    //   studentGender : [''],
    //   studentDob : [''],
    //   studentEmail : [''],
    //   studentMob : [''],
    //   studentSubscriptionEndDate : [''],
    //   guardianName : [''],
    //   guardianEmail : [''],
    //   guardianMob : [''],
    //   guardianRelationship : ['']
    // });
    // // this.student
  }

  switchTabs(t) {
    console.log(t);
    
    this.tabs = t;
  }

  // setEditForm(value){
  //   this.editForm = value;
  //   console.log(this.editForm);
  //   // return this.studentProfile.controls['studentName'].patchValue('ghv')
  // }

  // cancelEditForm(){
  //   this.editForm = "Student Profile";
  // }

  // updateProfile(){
  //   this.studentProfile.patchValue;
  //   console.log(this.studentProfile);
  // }

}
