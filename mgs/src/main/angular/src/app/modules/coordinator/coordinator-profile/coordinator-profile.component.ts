import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { BASE_URL } from 'src/app/services/app.constant';

@Component({
  selector: 'app-coordinator-profile',
  templateUrl: './coordinator-profile.component.html',
  styleUrls: ['./coordinator-profile.component.scss']
})
export class CoordinatorProfileComponent implements OnInit {
  BASE_URL: string;

  teacherInfo: any;
  teacherId: any;
  teacherDetails: any = {};

  profilePhotoForm: FormGroup;
  profileUpdateForm: FormGroup;
  files: any[];
  path: any;
  mobileNumber: any;
  email: any;
  setDisabled = true;
  oldMobNo: any;
  profileBrief: any;
  assignedClubsArr = [];
  assignedSocietyArr = [];
  club_loader: boolean = true;

  constructor(private formBuilder: FormBuilder, private teacherService: TeacherService, private alertService: AlertService) {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
  }

  ngOnInit() {
    // this.path = "assets/images/teacherprofile1.jpg";
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    // this.teacherId = this.teacherInfo['teacher'].id;
    this.teacherId = this.teacherInfo.id;
    this.teacherService.getProfile(this.teacherId).subscribe((res) => {
      this.teacherDetails = res;
      this.mobileNumber = res.mobileNumber;
      this.email = res.email;
      this.profileBrief = res.profileBrief;
      // this.path = res.imagePath;
      if (res.imagePath)
        this.path = res.imagePath;
      this.getAllClubs();

    },
      (err) => console.log(err)
    );

    this.profilePhotoForm = this.formBuilder.group({
      profilePic: []
    })

    this.profileUpdateForm = this.formBuilder.group({
      id: this.teacherId,
      mobileNumber: [],
      email: [],
      profileBrief: []
    })

    // this.getAllClubs();
  }

  // //get list of assigned/supervised Clubs and Society
  // getAllClubs() {
  //   this.teacherService.getAssignedClubs().subscribe(res => {
  //     this.assignedClubsArr = res.filter((e) => (e.clubOrSociety == "Club"));
  //     this.assignedSocietyArr = res.filter((e) => (e.clubOrSociety == "Society"));
  //   }, (err) => { console.log(err); });
  // }

  getAllClubs() {
    this.club_loader = false;
    if (this.teacherDetails.activityAndGrades) {
      this.assignedClubsArr = this.teacherDetails.activityAndGrades.filter((e) => (e.clubOrSociety == "Club"));
      this.assignedSocietyArr = this.teacherDetails.activityAndGrades.filter((e) => (e.clubOrSociety == "Society"));
    }
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
    this.teacherService.putProfilePhoto(formData).subscribe((res) => {
      this.teacherDetails = res;
      this.alertService.showSuccessAlert("Profile Photo Updated");
      console.log("Profile Photo Changed");
    }, (err) => {
      console.log(err);
    })
  }

  updateProfile() {
    if (this.setDisabled == false) {
      if (this.mobileNumber.length == 10) {

        this.profileUpdateForm.value.mobileNumber = this.mobileNumber;
        this.profileUpdateForm.value.email = this.email;
        this.profileUpdateForm.value.profileBrief = this.profileBrief;
        this.alertService.showLoader("");
        this.teacherService.updateProfile(this.teacherId, this.profileUpdateForm.value).subscribe((res) => {
          this.alertService.showSuccessToast("Profile Updated");
          this.setDisabled = true;
        }, (err) => {
          console.log(err);
          this.setDisabled = false;
          this.errorMessage(err);
        });
      }
    } else {

      // this.alertService.showErrorAlert("Fill the proper details");
      this.setDisabled = false;
    }
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
