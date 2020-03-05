import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { StudentService } from 'src/app/services/student.service';
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
    private studentService: StudentService, private alertService: AlertService,
    private router: Router) { }

  ngOnInit() {
    this.studentService.getSchools("/schools").subscribe(
      (res) => { this.schoolsList = res },
      (err) => console.log(err))

    this.teacherSignup = this.formBuilder.group({
      name: [('')],
      dob: [('')],
      email: [('')],
      mobileNumber: [('')],
      qualification: [('')],
      gender: [('')],
      activityIds: [('')],
      gradeIds: [('')],
      schoolId: [('')],
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

  setIsCoach(){
    this.teacherSignup.value.isCoach = "True"
  }

  onSubmit(){
    // const payload =  this.teacherSignup.value;
    // console.log(this.teacherSignup);

    const time = this.teacherSignup.value.dob + " 00:00:00";
    this.teacherSignup.value.dob = time;
    console.log(this.teacherSignup.value);
    
    this.studentService.uploadTeacherDetails(this.teacherSignup.value).subscribe((res) => {
      this.alertService.showSuccessToast('SignUp Successfully')
      this.router.navigate(['./login'])
    },
    (err) => console.log(err) );
  }

}
