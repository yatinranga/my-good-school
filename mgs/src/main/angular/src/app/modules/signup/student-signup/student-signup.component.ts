import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormBuilder, FormArray, FormControlName, Validators } from '@angular/forms';
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
  gradesList = [];

  constructor(private formBuilder: FormBuilder, private adminService: AdminService,
    private studentService: StudentService, private alertService: AlertService,
    private router: Router) { }

  ngOnInit() {
    this.studentService.getSchools("/schools").subscribe(
      (res) => {this.schoolsList = res},
      (err) => console.log(err))

    this.studentSignup = new FormGroup({
      name: new FormControl(null,[Validators.required]),
      dob: new FormControl(null,[Validators.required]),
      email: new FormControl(null,[Validators.required,Validators.email]),
      mobileNumber: new FormControl(null,[Validators.min(10)]),
      gender: new FormControl(null,[Validators.required]),
      schoolId: new FormControl(null,[Validators.required]),
      gradeId : new FormControl(null,[Validators.required]),
      sessionStartDate: new FormControl(null),
      subscriptionEndDate: new FormControl(null),
      guardians: new FormArray(this.giveGuardianFormArray())
    });
  }

  get guardians() { return this.studentSignup.get('guardians') as FormArray }

  giveGuardianFormArray() {
    const arr = [];
    return arr;
  }

  addGuardian() {
    console.log(this.studentSignup);
    const fg = new FormGroup({
      name: new FormControl(null),
      email: new FormControl(null),
      gender: new FormControl(null),
      mobileNumber: new FormControl(null),
      relationship: new FormControl(null)
    });
    (this.studentSignup.get('guardians') as FormArray).push(fg);
  }

  deleteGuardian(index){
    console.log("Delete Button")
    const guardians = <FormArray>this.studentSignup.controls['guardians'];
    guardians.removeAt(index)
  }

  onSubmit() {
    const time = this.studentSignup.value.dob + " 00:00:00";
    this.studentSignup.value.dob = time;
    console.log(time);
    // time.toString().append(" 00:00:00");
    // this.studentSignup.value.dob = time;
    const payload =  this.studentSignup.value;
    console.log(payload);
    this.studentService.uploadStudentDetails(payload).subscribe((res) => {
      this.alertService.showSuccessAlert(""); 
      this.router.navigate(['./login'])
    },
    (err) => console.log(err) )
  }

  getGrades(schoolId){
    this.studentService.getGradesOfSchool(schoolId).subscribe((res) => {
      this.gradesList = res;
    },
    (err) => console.log(err));
  }
}
