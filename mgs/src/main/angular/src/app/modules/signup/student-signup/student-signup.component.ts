import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormBuilder, FormArray, FormControlName } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-student-signup',
  templateUrl: './student-signup.component.html',
  styleUrls: ['./student-signup.component.scss']
})
export class StudentSignupComponent implements OnInit {

  studentSignup: FormGroup;
  schools = [];
  guardianArr: FormArray;
  // guardian : FormGroup = {
  //   guardianName : FormControl(''),
  //   guardianEmail : "",
  //   guardianGender : "",
  //   guardianMob : "",
  //   relationship : ""     
  // };

  constructor(private formBuilder: FormBuilder, private adminService: AdminService,
    private studentService: StudentService) { }

  ngOnInit() {
    // this.adminService.getSchools("/schools").
    // subscribe(
    //   res => this.schools = res,
    //   err => console.log(err)
    // );

    this.studentSignup = new FormGroup({
      name: new FormControl(''),
      dob: new FormControl(''),
      email: new FormControl(''),
      mob: new FormControl(''),
      gender: new FormControl(''),
      schoolId: new FormControl(''),
      sessionStartDate: new FormControl(''),
      subscriptionEndDate: new FormControl(''),
      guardians: new FormArray(this.giveGuardianFormArray())
    });
    console.log(this.studentSignup);

  }

  get guardians() { return this.studentSignup.get('guardians') as FormArray }

  getSchoolID(schoolName) {
    this.studentSignup.value['schoolId'] = this.schools.filter((ele) => ele.name === schoolName)[0].id;
  }

  giveGuardianFormArray() {
    const arr = [];
    const fg = new FormGroup({
      name: new FormControl(''),
      email: new FormControl(''),
      gender: new FormControl(''),
      mobileNumber: new FormControl(''),
      relationship: new FormControl('')
    });
    arr.push(fg);
    return arr;
  }

  addGuardian() {
    console.log(this.studentSignup);
    // const arr = [];
    const fg = new FormGroup({
      name: new FormControl(''),
      email: new FormControl(''),
      gender: new FormControl(''),
      mobileNumber: new FormControl(''),
      relationship: new FormControl('')
    });
    (this.studentSignup.get('guardians') as FormArray).push(fg);
  }

  onSubmit() {
    const payload = { studentSignUp: this.studentSignup.value };
    this.studentService.uploadStudentDetails(payload);

  }

}
