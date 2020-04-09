import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-teacher-profile',
  templateUrl: './teacher-profile.component.html',
  styleUrls: ['./teacher-profile.component.scss']
})
export class TeacherProfileComponent implements OnInit {
  teacherInfo: any;
  teacherId: any;
  teacherDetails = {};

  profilePhotoForm: FormGroup;
  files: any[];
  path: any;
  mobileNumber: any;
  email: any;
  isEditable = true;

  constructor(private formBuilder: FormBuilder, private teacherService: TeacherService, private alertService : AlertService) { }

  ngOnInit() {
    this.path = "assets/images/teacherprofile1.jpg";
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.teacherId = this.teacherInfo['teacher'].id;
    this.teacherService.getProfile(this.teacherId).subscribe((res) => {
      this.teacherDetails = res;
      this.mobileNumber = res.mobileNumber;
      this.email = res.email;
      console.log(res);
    },
    (err) => console.log(err)
    );

    this.profilePhotoForm = this.formBuilder.group({
      profilePic: []
    })
  }
  
  // Select Profile Photo
  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.files = [...event.target.files];

      const file = this.files[0];
      this.profilePhotoForm.value['profilePic'] = file;

      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);

      reader.onload = (event: any) => { this.path = event.target.result; }
    } else {
      this.path = null;
    }

    this.editProfilePhoto();
  }

  // Edit Profile Photo of Student
  editProfilePhoto() {
    this.alertService.showSuccessAlert("Profile Photo Updated");
    const formData = new FormData();
    formData.append('profilePic', this.profilePhotoForm.value.profilePic);
    this.teacherService.putProfilePhoto(formData).subscribe((res) => {
      console.log("Profile Photo Changed");
      console.log(res);
    }, (err) => {
      console.log(err);
    })
  }

  setEditable(){
    this.isEditable = !this.isEditable;
  }

  temp(){
    console.log("MOBILE changed");
  }

  updateMobileNo(){

    // this.teacherService.putMobileNumber(this.teacherId,json).subscribe((res) =>{
    //   console.log(res);
    // },(err) => {console.log(err)};)
  }

  // Edit Mobile Number
  // editMobileNumber(){
  //   this.teacherService.putMobileNumber().subscribe((res) => {
  //     console.log(res);
  //   },(err) => {console.log(err)});
  // }

}
