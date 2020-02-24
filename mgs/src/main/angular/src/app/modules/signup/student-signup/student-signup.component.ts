import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormBuilder, FormArray, FormControlName } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-student-signup',
  templateUrl: './student-signup.component.html',
  styleUrls: ['./student-signup.component.scss']
})
export class StudentSignupComponent implements OnInit {

  studentSignup: FormGroup;
  schoolsList = [];

  constructor(private formBuilder: FormBuilder, private adminService: AdminService,
    private studentService: StudentService, private alertService: AlertService,
    private router: Router) { }

  ngOnInit() {
    this.studentService.getSchools("/schools").subscribe(
      (res) => {this.schoolsList = res},
      (err) => console.log(err))

    this.studentSignup = new FormGroup({
      name: new FormControl(''),
      dob: new FormControl(''),
      email: new FormControl(''),
      mobileNumber: new FormControl(''),
      gender: new FormControl(''),
      schoolId: new FormControl(''),
      sessionStartDate: new FormControl(''),
      subscriptionEndDate: new FormControl(''),
      guardians: new FormArray(this.giveGuardianFormArray())
    });
    console.log(this.studentSignup);
  }

  get guardians() { return this.studentSignup.get('guardians') as FormArray }

  // getSchoolID(schoolName) {
  //   this.studentSignup.value['schoolId'] = this.schools.filter((ele) => ele.name === schoolName)[0].id;
  // }

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
    const payload =  this.studentSignup.value;
    console.log(payload)
    this.studentService.uploadStudentDetails(payload).subscribe((res) => {
      this.alertService.showSuccessToast('SignUp Successfully')
      this.router.navigate(['./login'])
    },
    (err) => console.log(err) )
  }
}