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
  profilePhotoForm: FormGroup;
  editForm = 'Student Profile';
  studentDetails = {};
  studentInfo: any;
  studentId: any;
  tabs = 'guardian0';

  files: any[];
  path: any;

  constructor(private formBuilder: FormBuilder, private studentService: StudentService) { }

  ngOnInit() {
    this.path = "assets/images/childprofile.jpg";
    // this.path = "assets/images/boy.jpg";
    this.profilePhotoForm = this.formBuilder.group({
      profilePic: []
    })

    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.studentService.getProfile(this.studentId).subscribe((res) => {
      this.studentDetails = res;
      console.log(this.studentDetails);
      // Profile Photo is there it will be added
      // if(res.profileImage){
      //   this.path  = this.studentDetails["profileImage"];
      // }
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
    const formData = new FormData();
    formData.append('profilePic', this.profilePhotoForm.value.profilePic);
    this.studentService.putProfilePhoto(this.studentId, formData).subscribe((res) => {
      console.log("Profile Photo Changed");
      console.log(res);
    }, (err) => {
      console.log(err);
    })
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
