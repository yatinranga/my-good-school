import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-activity-details',
  templateUrl: './activity-details.component.html',
  styleUrls: ['./activity-details.component.scss']
})
export class ActivityDetailsComponent implements OnInit {

  clubObject: any;
  studentInfo: any;
  schoolId = "";
  supervisorId = ""; 
  grades = [];
  gradeId = "";
  filterVal = "";

  coaches = [];
  students = [];
  copyStuArr = [];
  clubSchedule = [];
  copySchedule = [];
  stu_loader = false;
  sup_loader = true;
  enrollSch_loader = false;
  constructor(private studentService: StudentService) { }

  ngOnInit() {

    // console.log(history.state);
    // this.clubObject = history.state;
    this.clubObject = JSON.parse(localStorage.getItem('club'));
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.student.schoolId;
    this.getCoaches(this.clubObject.id);
    this.getGrades(this.schoolId);

  }

  getCoaches(actiId){
    this.sup_loader = true;
    this.studentService.getCoach(this.schoolId, actiId).subscribe((res) => {
      this.coaches = res;
      console.log(res);
      this.sup_loader = false;
    }, (err) => {
      console.log(err);
      this.sup_loader = false;
    })
  }

  // List of ALL Grades of a School
  getGrades(schoolId) {
    this.studentService.getGradesOfSchool(schoolId).subscribe((res) => {
      this.grades = res;
    }, (err) => { console.log(err) });
  }

  getSupervisorSession(supervisor_obj){
    this.enrollSch_loader = true;
    this.supervisorId = supervisor_obj.id;
    this.getStudents(supervisor_obj.id);

    this.studentService.getSupervisorSchedule(this.clubObject.id,supervisor_obj.id).subscribe((res) => {
      console.log(res.sessions);
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.enrollSch_loader = false;
    },(err) => {console.log(err);
    this.enrollSch_loader = false;});

  }

      // List of Student of selected Club/Society under specific Supervisor
      getStudents(supervisorId) {
        this.stu_loader = true; // Student loader
        this.studentService.getSupervisorStudent(this.clubObject.id,supervisorId).subscribe((res) => {
          this.students = res;
          this.copyStuArr = Object.assign([], res);
          console.log(res);
          this.stu_loader = false;
        }, (err) => {
          console.log(err);
          this.stu_loader = false;
        });
      }

  filterStudent(val){
    this.students = this.filter(Object.assign([], this.copyStuArr), val, "Student");
  }

    // Filter session on the bases of ALL, UPCOMING and ENDED
    filterSession(val) {
      this.clubSchedule = this.filter(Object.assign([], this.copySchedule), val, "Session");
    }

  // Actual Filtering of Sessions on the basis of type
  filter(array: any[], value: string , type:string) {
    let filterSessionArr = [];
    if(type=="Session"){
      if (value)
        filterSessionArr = array.filter(e => e.responses[0].status == value);
      else
        filterSessionArr = array;
    }
    if(type=="Student"){
      if (value)
        filterSessionArr = array.filter(e => e.gradeId == value);
      else
        filterSessionArr = array;
    }

    return filterSessionArr;
  }

}
