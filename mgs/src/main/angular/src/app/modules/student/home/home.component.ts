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
  copySchedule = [];

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
  clubSchedule = [];
  enrollSch_loader = false;

  // Details of All Club/Society Modal Variable
  clubName = "";
  sessionView = false;

  stu_loader = false;
  sup_loader = false;
  csup_loader = false;

  filterVal = ""; // filter the activity by ALL, UPCOMING and ENDED

  constructor(private studentService: StudentService, public alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.student.schoolId;
    this.studentName = this.studentInfo.student.name;
    this.getActivity(this.schoolId); // ALL Activites of School
    // this.getGrades(this.schoolId); // ALL Grades of School
    this.getEnrolledClub(); // List of Enrolled Club
    this.getSessionDetails(); // List of Scheduled Session of a WEEK
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
      console.log(res.sessions);
      this.sessionsArr = res.sessions;
    }, (err) => { console.log(err); });
  }

  // List of ALL Grades of a School
  // getGrades(schoolId) {
  //   this.studentService.getGradesOfSchool(schoolId).subscribe((res) => {
  //     this.grades = res;
  //   }, (err) => { console.log(err) });
  // }

    // Schedule of Selected Enrolled Club/Society
    viewEnrolledSchedule(myClub) {
      console.log(myClub.name + " clicked");
      this.enrollSch_loader = true;
      this.clubSchedule = [];
      this.enrolledName = "";
      this.filterVal = "";
      $('#enrolledScheduleModal').modal('show');
      this.enrolledName = myClub.name;
      this.getScheduleOfClub(myClub.id);
    }

      // Get Session Schedule of selected Club/Society
  getScheduleOfClub(clubId, duration?) {
    this.enrollSch_loader = true;
    this.clubSchedule = [];
    this.studentService.getEnrolledClubSession(clubId).subscribe((res) => {
      console.log(res.sessions);
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.enrollSch_loader = false
    }, (err) => {
      console.log(err);
      this.enrollSch_loader = false;
    });
  }  

  // Filter session on the bases of ALL, UPCOMING and ENDED
  filterSession(val) {
    this.clubSchedule = this.filter(Object.assign([], this.copySchedule), val);
  }

  // Actual Filtering of Sessions on the basis of ALL, UPCOMING and ENDED
  filter(array: any[], value: string) {
    let filterSessionArr = [];
    if (value)
      filterSessionArr = array.filter(e => e.responses[0].status == value);
    else
      filterSessionArr = array;

    return filterSessionArr;
  }

   // Details of All Clubs and Societies
   clubDetails(clubObj) {
    $('#clubDetailsModal').modal('show');
    this.clubName = clubObj.name;
    this.clubId = clubObj.id;
    this.supervisorId = "";
    this.sessionView = false;
    this.getCoaches(clubObj.id);
    // this.getScheduleOfClub(clubObj.id);
    console.log(clubObj);
  }

  // List of Supervisor of Selected Club/Society
  getCoaches(actiId) {
    // this.getStudents(actiId);
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

  // List of Supervisor of a particular club of activity during Registration
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

 



  // List of Student of selected Club/Society
  getStudents(actiId) {
    this.stu_loader = true;
    this.studentService.getActivityStudent(actiId).subscribe((res) => {
      this.students = res;
      // this.copyStuArr = Object.assign([], res);
      console.log(res);
      this.stu_loader = false;
    }, (err) => {
      console.log(err);
      this.stu_loader = false;
    });
  }

  // Session Schedule of a particular Student
  getSupervisorSession(obj_sup){
    // console.log(obj_sup.id);
    this.clubSchedule = [];
    this.sessionView = true;
    this.enrollSch_loader = true;
    this.supervisorId = obj_sup.id;
    this.studentService.getSupervisorSchedule(this.clubId,obj_sup.id).subscribe((res) => {
      console.log(res.sessions);
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.enrollSch_loader = false;
    },(err) => {console.log(err);
    this.enrollSch_loader = false;});
  }





}
