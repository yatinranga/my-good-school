import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
import { BASE_URL } from 'src/app/services/app.constant';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  BASE_URL: string;

  studentProfile: FormGroup;
  profilePhotoForm: FormGroup;
  editForm = 'Student Profile';
  studentDetails:any = {};
  studentInfo: any;
  studentId: any;
  tabs = 'guardian0';

  files: any[];
  path: any;
  enrolledClubsArr = [];

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
   }

  ngOnInit() {

    this.profilePhotoForm = this.formBuilder.group({
      profilePic: []
    })

    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo.id;
    this.studentService.getProfile(this.studentId).subscribe((res) => {
      this.studentDetails = res;
      console.log(this.studentDetails);
    },
      (err) => console.log(err)
    );   
  }

  switchTabs(t) {
    console.log(t);
    this.tabs = t;
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
    this.alertService.showLoader("");
    const formData = new FormData();
    formData.append('profilePic', this.profilePhotoForm.value.profilePic);
    this.studentService.putProfilePhoto(this.studentId, formData).subscribe((res) => {
      this.alertService.showSuccessAlert("Profile Photo Updated");
      this.studentDetails = res;
      console.log("Profile Photo Changed");
    }, (err) => {
      console.log(err);
      this.errorMessage(err);
    })
  }

  /** Handling Error */
  errorMessage(err) {
    if (err.status == 400) {
      this.alertService.showMessageWithSym(err.msg, "", "info");
    }
    else {
      this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
    }
  }
}