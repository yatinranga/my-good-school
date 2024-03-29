import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-teacher-signup',
  templateUrl: './teacher-signup.component.html',
  styleUrls: ['./teacher-signup.component.scss']
})

export class TeacherSignupComponent implements OnInit {

  teacherSignup: FormGroup;
  schoolsList = [];
  schoolGrades = [];
  schoolActivities = [];

  constructor(private formBuilder: FormBuilder, private adminService: AdminService,
    private teacherService: TeacherService, private alertService: AlertService,
    private router: Router) { }

  ngOnInit() {
    this.teacherService.getSchools("/schools").subscribe(
      (res) => { this.schoolsList = res },
      (err) => console.log(err))

    this.teacherSignup = this.formBuilder.group({
      name: [null,[Validators.required,Validators.pattern('[a-zA-Z ]*')]],
      dob: [null,[Validators.required]],
      email: [null,[Validators.required,Validators.email]],
      mobileNumber: [null,[Validators.required,Validators.minLength(10),Validators.pattern("[0-9]*")]],
      qualification: [null],
      gender: [null],
      activityIds: [null],
      gradeIds: [null],
      schoolId: [null],
      isCoach: [('False')],
      isClassTeacher: [('True')],      
    })
  }

  get activitiyIds() {
    return this.teacherSignup.get('activitiyIds') as FormArray 
  }

  getSchoolActivities(selectedSchoolId){
    this.adminService.getActivities(selectedSchoolId).subscribe((res) => {
      this.schoolActivities = res;
    },
    (err) => console.log(err));
    
    this.getSchoolGrades(selectedSchoolId);      
  }

  getSchoolGrades(selectedSchoolId){
    this.adminService.getGrades(selectedSchoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
    (err) => console.log(err));
  }

  setIsCoach(v:any[]){
    if(v.length == 0){
      this.teacherSignup.value.isCoach = "False"
    } else {
      this.teacherSignup.value.isCoach = "True"
    }
  }

  onSubmit(){
    // const payload =  this.teacherSignup.value;
    // console.log(this.teacherSignup);

    const time = this.teacherSignup.value.dob + " 00:00:00";
    this.teacherSignup.value.dob = time;
    console.log(this.teacherSignup.value);
    
    this.teacherService.uploadTeacherDetails(this.teacherSignup.value).subscribe((res) => {
      this.alertService.showSuccessAlert(""); 
      this.router.navigate(['./login']);
    },
    (err) => console.log(err) );
  }

}
