import { Component, OnInit } from '@angular/core';
import { AlertService } from 'src/app/services/alert.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-school-profile',
  templateUrl: './school-profile.component.html',
  styleUrls: ['./school-profile.component.scss']
})
export class SchoolProfileComponent implements OnInit {

  schoolInfo: any;
  schoolDetails = {};
  schoolId: string = "";
  setDisabled = true;
  address = "";
  name = "";
  profileUpdateForm: FormGroup;
  gradesArr = [];
  allActiArr=[];
  clubsArr=[];
  societyArr = [];

  constructor(private schoolService: SchoolService, private alertService: AlertService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.schoolInfo = JSON.parse(localStorage.getItem('user_info'));
    // this.schoolId = this.schoolInfo.id;
    this.schoolId = "lNw8Vpcn";
    this.getProfileDetails(this.schoolId);

    this.profileUpdateForm = this.formBuilder.group({
      // id: this.schoolId,
      name: [],
      address: []
    })
  }

  // get School Profile
  getProfileDetails(schoolId) {
    this.schoolService.getProfile(schoolId).subscribe((res) => {
      this.schoolDetails = res;
      this.name = res.name;
      this.address = res.address;
      this.allActiArr = res.activities;
      console.log(res);
    }, (err) => { console.log(err); });
  }


  updateProfile(){
    if(this.setDisabled== false){
      this.setDisabled = true;
      // this.profileUpdateForm.value.name = this.name;
      // this.profileUpdateForm.value.address = this.address;
      // this.schoolService.updateProfile(this.schoolId,this.profileUpdateForm.value).subscribe((res)=>{
      //   console.log(res);
      //   this.setDisabled = true;
      // },(err)=>{console.log(err);})
    } else {
      this.setDisabled = false;
    }

  }

}
