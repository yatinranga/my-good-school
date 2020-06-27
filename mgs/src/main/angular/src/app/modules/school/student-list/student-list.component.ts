import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { SchoolService } from 'src/app/services/school.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.scss']
})
export class StudentListComponent implements OnInit {

  constructor(private schoolService: SchoolService, private alertService: AlertService) { }
  col = "col-12";
  showDetails:boolean = false
  studentsArr: any = [];
  copyStudentArr = [];
  student_loader = false;
  student_obj: any; // Used to transfer object to Student Details Component
  schoolGrades = [];
  adminInfo: any;
  gradeId:string = "";
  search="" // USed for Search
  studentId:any;  

  ngOnInit() {
    this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getAllStudents();
    this.getSchoolGrades();
  }

  /** When Enrolled Clubs of particukar of student are shown */
  updatedTable($event){
    this.getAllStudents();
  }

  /** Get all Grades of the School */
  getSchoolGrades(){
    this.schoolService.getAllGrades(this.adminInfo.schoolId).subscribe((res) => {
      this.schoolGrades = res;
    }, (err) => { console.log(err); })
  }

  filterStudents(){
    this.studentsArr = this.filter(Object.assign([], this.copyStudentArr));
  }

  filter(array: any[]){
    let filterStuArr = []
    if(this.gradeId){
        filterStuArr = array.filter(e => e.gradeId && e.gradeId == this.gradeId);
    }
    else{
      filterStuArr = array;
    }
    return filterStuArr;
  }

  /** Set Show Details */
  setShowDetails(val: boolean, student_obj?){
    this.student_obj = student_obj;
    this.studentId = student_obj.id;
    console.log(student_obj);
    this.showDetails = val;
    this.showDetails ? (this.col = "col-7") : (this.col="col-12");
  }

  /** Get List of All Students */
  getAllStudents() {
    this.student_loader = true;
    this.schoolService.getStudents().subscribe((res) => {
      this.studentsArr = res;
      this.copyStudentArr = Object.assign([], res);
      console.log(res);
      this.student_loader = false;

    }, (err) => {
      this.student_loader = false;
      console.log(err);
      if (err.status == 400) {
        this.alertService.showMessageWithSym(err.msg, "", "info");
      }
      else {
        this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
      }
    })
  }

}
