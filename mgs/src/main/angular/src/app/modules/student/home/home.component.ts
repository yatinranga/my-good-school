import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
declare let $: any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  activities = [];
  coaches = [];
  students = [];
  grades = [];
  copyStuArr = [];

  enrolledClubsArr = [];
  clubSupervisor = [];

  studentInfo: any;
  studentId: any;
  schoolId: any;

  activityId = "";
  gradeId = "";
  clubId = "";
  supervisorId = "";

  stu_loader = false;
  sup_loader = false;
  csup_loader = false;

  constructor(private studentService: StudentService, public alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.student.schoolId;
    this.getActivity(this.schoolId);
    this.getGrades(this.schoolId);
    this.getEnrolledClub();
  }

  // get List of Activities of School
  getActivity(schoolId) {
    this.studentService.getActivity(schoolId).subscribe((res) => {
      this.activities = res;
    },
      (err) => console.log(err)
    );
  }

  // get List of Supervisior of Selected Activity
  getCoaches(actiId) {
    this.getStudents(actiId);
    this.coaches = [];
    this.students = [];
    this.gradeId = "";
    this.sup_loader = true;
    this.studentService.getCoach(this.schoolId, actiId).subscribe((res) => {
      this.coaches = res;
      this.sup_loader = false;
    }, (err) => {
      console.log(err);
      this.sup_loader = false;
    })
  }

  // get List of Student of selected Activity
  getStudents(actiId) {
    this.stu_loader = true;
    this.studentService.getActivityStudent(actiId).subscribe((res) => {
      this.students = res;
      this.copyStuArr = Object.assign([], res);
      console.log(res);
      this.stu_loader = false;
    }, (err) => {
      console.log(err);
      this.stu_loader = false;
    });
  }

  getGrades(schoolId) {
    this.studentService.getGradesOfSchool(schoolId).subscribe((res) => {
      this.grades = res;
    }, (err) => { console.log(err) });
  }

  filterStudent() {
    this.students = this.filter(Object.assign([], this.copyStuArr));
  }

  // Actual Filtering on the basis of PSD , Focus Area and 4S
  filter(array: any[]) {
    let filterStudentArr = [];
    if (this.gradeId) {
      filterStudentArr = array.filter(e => e.gradeId == this.gradeId);
    } else {
      filterStudentArr = array;
    }
    return filterStudentArr;
  }

  // List of enrolled Clubs and Societies
  getEnrolledClub() {
    this.studentService.getAllEnrolledClub().subscribe(res => {
      console.log(res);
      this.enrolledClubsArr = res;
    }, (err) => { console.log(err) });
  }

  // List of Supervisor of a particular club of activity
  getSupervisor(actiId) {
    this.clubId = "";
    this.supervisorId = "";
    this.clubSupervisor = [];
    this.csup_loader = true;
    this.clubId = actiId;
    this.studentService.getCoach(this.schoolId, actiId).subscribe((res) => {
      this.clubSupervisor = res;
      console.log(res);
      this.csup_loader = false;
    }, (err) => {
      console.log(err);
      this.csup_loader = false;
    })
  }

  clubRegistration() {
    console.log(this.clubId);
    console.log("Supervisor- ",this.supervisorId);
    if (this.clubId && this.supervisorId) {
      this.alertService.showLoader("");
      this.studentService.postEnrollInClub(this.clubId, this.supervisorId).subscribe(res => {
        console.log(res)
        this.alertService.showSuccessAlert("Registration Successful");
        $('#registrationModal').modal('hide');
        $('.modal-backdrop').remove();
      }, (err) => { console.log(err); });
      
    } else {
      this.alertService.showErrorAlert("Fill all the details");
    }
  }

  clearSelected(){
    console.log("clear selection called");
    this.clubId = "";
    this.supervisorId = "";
  }


}
