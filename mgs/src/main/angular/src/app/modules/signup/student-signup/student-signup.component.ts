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
      name: new FormControl(null,[Validators.required,Validators.pattern('[a-zA-Z ]*')]),
      dob: new FormControl(null,[Validators.required]),
      email: new FormControl(null,[Validators.required,Validators.email]),
      mobileNumber: new FormControl(null,[Validators.minLength(10),Validators.pattern("[0-9]*")]),
      gender: new FormControl(null,[Validators.required]),
      schoolId: new FormControl(null,[Validators.required]),
      gradeId : new FormControl(null,[Validators.required]),
      sessionStartDate: new FormControl(null),
      subscriptionEndDate: new FormControl(null),
      guardians: new FormArray([])
    });
  }

  get guardians() { return this.studentSignup.get('guardians') as FormArray }


  addGuardian() {
    console.log(this.studentSignup.controls);
    if(this.guardians.valid){      
    // console.log(this.studentSignup);
    const fg = new FormGroup({
      name: new FormControl(null,[Validators.required,Validators.pattern('[a-zA-Z ]*')]),
      email: new FormControl(null,[Validators.required,Validators.email]),
      gender: new FormControl(null,[Validators.required]),
      mobileNumber: new FormControl(null,[Validators.required,Validators.minLength(10),Validators.pattern("[0-9]*")]),
      relationship: new FormControl(null,[Validators.required])
    });
    (this.studentSignup.get('guardians') as FormArray).push(fg);
  } else  {
    this.alertService.showErrorAlert("Kindly fill all the details of the Guardian");
  }
  }

  deleteGuardian(index){
    console.log("Delete Button")
    const guardians = <FormArray>this.studentSignup.controls['guardians'];
    guardians.removeAt(index)
  }

  onSubmit() {
    console.log(this.studentSignup.value.guardians.length);
    const time = this.studentSignup.value.dob + " 00:00:00";
    this.studentSignup.value.dob = time;
    console.log(time);
    // time.toString().append(" 00:00:00");
    // this.studentSignup.value.dob = time;
    const payload =  this.studentSignup.value;
    console.log(payload);
    this.alertService.showLoader("");
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
