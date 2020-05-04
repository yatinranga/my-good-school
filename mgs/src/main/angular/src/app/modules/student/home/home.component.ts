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

  sportArr = [];
  skillArr = [];
  serviceArr = [];
  studyArr = [];

  enrolledClubsArr = [];
  enrolledSocietyArr = [];
  sessionsArr = [];
  clubSupervisor = [];

  studentInfo: any;
  studentId: any;
  schoolId: any;
  studentName: any;

  activityId = "";
  gradeId = "";
  clubId = "";
  supervisorId = "";

  // Schedule Modal Variable
  enrolledName = "";
  sessionTitle = "";
  clubSchedule = [];
  enrollSch_loader = false;

  // Details of All Club/Society Modal Variable
  clubName = "";

  stu_loader = false;
  sup_loader = false;
  csup_loader = false;

  constructor(private studentService: StudentService, public alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.student.schoolId;
    this.studentName = this.studentInfo.student.name;
    this.getActivity(this.schoolId);
    this.getGrades(this.schoolId);
    this.getEnrolledClub();
    this.getSessionDetails();
  }

  // get List of Activities of School
  getActivity(schoolId) {
    this.studentService.getActivity(schoolId).subscribe((res) => {
      this.activities = res;
      this.sportArr = res.filter((e) => (e.fourS == 'Sport'));
      this.skillArr = res.filter((e) => (e.fourS == 'Skill'));
      this.serviceArr = res.filter((e) => (e.fourS == 'Service'));
      this.studyArr = res.filter((e) => (e.fourS == 'Study'));
    },
      (err) => console.log(err)
    );
  }

  // List of enrolled Clubs and Societies
  getEnrolledClub() {
    this.studentService.getAllEnrolledClub().subscribe(res => {
      this.enrolledClubsArr = res.filter(e => e.clubOrSociety == "Club");
      this.enrolledSocietyArr = res.filter(e => e.clubOrSociety == "Society");
    }, (err) => { console.log(err) });
  }

  // List of Sessions in current week
  getSessionDetails() {
    this.studentService.getSession("week").subscribe((res) => {
      console.log(res);
      this.sessionsArr = res;
    }, (err) => { console.log(err); });
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

  // Register in specific club/society
  clubRegistration() {
    console.log(this.clubId);
    console.log("Supervisor- ", this.supervisorId);
    if (this.clubId && this.supervisorId) {
      this.alertService.showLoader("");
      this.studentService.postEnrollInClub(this.clubId, this.supervisorId).subscribe(res => {
        console.log(res)
        this.alertService.showSuccessAlert("Registration Successful");
        $('#registrationModal').modal('hide');
        $('.modal-backdrop').remove();
        this.clearSelected();
      }, (err) => { console.log(err); });

    } else {
      this.alertService.showErrorAlert("Fill all the details");
    }
  }

  clearSelected() {
    console.log("clear selection called");
    this.clubId = "";
    this.supervisorId = "";
    this.clubSupervisor = [];
  }

  // Schedule of Selected Enrolled Club/Society
  viewEnrolledSchedule(myClub) {
    console.log(myClub.name + " clicked");
    this.enrollSch_loader = true;
    this.clubSchedule = [];
    this.enrolledName = "";
    this.sessionTitle = "";
    $('#enrolledScheduleModal').modal('show');
    this.enrolledName = myClub.name;
    this.sessionTitle = myClub.title;
    this.getScheduleOfClub(myClub.id);
  }

  // Details of All Clubs and Societies
  clubDetails(clubObj){
    $('#clubDetailsModal').modal('show');
    this.clubName=clubObj.name;
    this.getCoaches(clubObj.id);
    this.getScheduleOfClub(clubObj.id);
    console.log(clubObj);
  }
  
  // List of Supervisor of Selected Club/Society
  getCoaches(actiId) {
    this.getStudents(actiId);
    this.coaches = [];
    this.students = [];
    this.gradeId = "";
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

  // List of Student of selected Club/Society
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

  // Get Session Schedule of selected Club/Society
  getScheduleOfClub(clubId){
    this.enrollSch_loader = true;
    this.clubSchedule = [];
    this.studentService.getEnrolledClubSession(clubId, "month").subscribe((res) => {
      console.log(res);
      this.clubSchedule = res;
      this.enrollSch_loader = false
    }, (err) => {
      console.log(err);
      this.enrollSch_loader = false;
    });
  }



}
