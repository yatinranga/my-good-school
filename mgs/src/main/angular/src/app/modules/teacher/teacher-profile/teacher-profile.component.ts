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
  teacherDetails: any;

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

  constructor(private formBuilder: FormBuilder, private teacherService: TeacherService, private alertService: AlertService) { }

  ngOnInit() {
    this.path = "assets/images/teacherprofile1.jpg";
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    // this.teacherId = this.teacherInfo['teacher'].id;
    this.teacherId = this.teacherInfo.id;
    this.teacherService.getProfile(this.teacherId).subscribe((res) => {
      this.teacherDetails = res;
      this.mobileNumber = res.mobileNumber;
      this.email = res.email;
      this.profileBrief = res.profileBrief;
      console.log(res);
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
    console.log("Ther is goes: ", this.teacherDetails['activityAndGrades']);
    this.assignedClubsArr = this.teacherDetails.activityAndGrades.filter((e) => (e.clubOrSociety == "Club"));
    this.assignedSocietyArr = this.teacherDetails.activityAndGrades.filter((e) => (e.clubOrSociety == "Society"));
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

  // updateProfile(){
  //   let oldMobile = this.mobileNumber;
  //   if(this.setDisabled == false){

  //   }
  //   return !this.setDisabled;
  // }

  temp(e) {
    // console.log(e);
  }

  updateProfile() {
    if (this.setDisabled == false) {
      if (this.mobileNumber.length == 10) {

        this.profileUpdateForm.value.mobileNumber = this.mobileNumber;
        this.profileUpdateForm.value.email = this.email;
        this.profileUpdateForm.value.profileBrief = this.profileBrief;
        this.alertService.showLoader("");
        this.teacherService.updateProfile(this.teacherId, this.profileUpdateForm.value).subscribe((res) => {
          console.log(res);
          this.alertService.showSuccessToast("Profile Updated");
          this.setDisabled = true;
        }, (err) => {
          console.log(err);
          this.setDisabled = false;
        });
      }
    } else {

      // this.alertService.showErrorAlert("Fill the proper details");
      this.setDisabled = false;
    }
  }

  // updateMobileNo() {
  //   if (this.setDisabled == false) {
  //     if (this.oldMobNo != this.mobileNumber && this.mobileNumber.length == 10) {
  //       this.profileUpdateForm.value.mobileNumber = this.mobileNumber;
  //       this.alertService.showLoader("");

  //       this.teacherService.putMobileNumber(this.teacherId, this.profileUpdateForm.value).subscribe((res) => {
  //         console.log(res);
  //         this.alertService.showSuccessToast("Mobile Phone Updated");
  //         this.setDisabled = true;
  //       }, (err) => {
  //         console.log(err);
  //         this.setDisabled = false;
  //       });
  //     } else {
  //       if (this.mobileNumber.length == 10) {
  //         this.oldMobNo = this.mobileNumber;
  //         this.setDisabled = true;
  //       } else {
  //         this.alertService.showErrorAlert("Mobile Number can be less than 10");
  //       }
  //     }
  //   } else {
  //     this.oldMobNo = this.mobileNumber;
  //     this.setDisabled = false;
  //   }
  // }

  // Edit Mobile Number
  // editMobileNumber(){
  //   this.teacherService.putMobileNumber().subscribe((res) => {
  //     console.log(res);
  //   },(err) => {console.log(err)});
  // }

}
