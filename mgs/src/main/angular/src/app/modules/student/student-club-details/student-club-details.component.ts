import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-activity-details',
  templateUrl: './student-club-details.component.html',
  styleUrls: ['./student-club-details.component.scss']
})
export class StudentClubDetailsComponent implements OnInit {


  clubObject: any;
  studentInfo: any;
  modalClass = "";
  schoolId = "";
  supervisorId = ""; 
  grades = [];
  gradeId = "";
  filterVal = "";
  supervisorName = "";

  coaches = [];
  students = [];
  copyStuArr = [];
  clubSchedule = [];
  copySchedule = [];
  stu_loader = false;
  sup_loader = true;
  enrollSch_loader = false;
  constructor(private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {

    // console.log(history.state);
    // this.clubObject = history.state;
    this.clubObject = JSON.parse(localStorage.getItem('club'));
    this.setClass(this.clubObject.fourS);
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.student.schoolId;
    this.getCoaches(this.clubObject.id);
    this.getGrades(this.schoolId);

  }
  setClass(club_type){
    console.log(club_type);
    switch(club_type){
      case 'Sport': this.modalClass = "sportmodal"; break;
      case 'Skill': this.modalClass = "skillmodal"; break;
      case 'Service': this.modalClass = "servicemodal"; break;
      case 'Study': this.modalClass = "studymodal"; break;
    }
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
    this.supervisorName = supervisor_obj.name;
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

  clubRegBtn() {
    let a = "Activity : " + this.clubObject.name + "\n, Supervisor : " + this.supervisorName;
    this.alertService.confirmWithoutLoader("info", "Are you sure you want to register ?", a, "Yes").then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        this.studentService.postEnrollInClub(this.clubObject.id, this.supervisorId).subscribe(res => {
          console.log(res)
          this.alertService.showSuccessAlert("Request Sent");
        }, (err) => { console.log(err); });
      }
    })

  }

}