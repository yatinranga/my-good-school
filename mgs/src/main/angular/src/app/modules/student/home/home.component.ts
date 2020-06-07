import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
import { Router } from '@angular/router';

// import { ActivityDetailsComponent } from 'src/app/modules/student/activity-details/activity-details.component.ts';
declare let $: any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  studentInfo: any;
  studentId: any;
  schoolId: any;
  studentName: any;

  modalClass = ""; // ADD class according to Activity
  allActivities = []; // List of All Club/Society in a school
  enrolledClubsArr = [];
  enrolledSocietyArr = [];
  coaches = [];
  students = [];
  grades = [];
  sessionsArr = []; // List of Session for a week
  copySchedule = []; // used for filtering Session

  sportArr = [];
  skillArr = [];
  serviceArr = [];
  studyArr = [];

  clubSupervisor = [];

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
  supervisorName = "";
  sessionView = false;
  copyStuArr = [];

  student_loader = false;
  supervisor_loader = false;
  csup_loader = false;

  highlightClubArr = [];
  highlightSocietyArr = [];
  clubObject = {};
  filterVal = ""; // filter the activity by ALL, UPCOMING and ENDED

  constructor(private studentService: StudentService, public alertService: AlertService, private router: Router) { }


  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.schoolId;
    this.studentName = this.studentInfo.name;
    // this.getGrades(this.schoolId); // ALL Grades of School
    this.getEnrolledClub(); // List of Enrolled Club
    this.getSessionDetails(); // List of Scheduled Session of a WEEK
    this.getActivity(this.schoolId); // ALL Activites of School
    // history.pushState({data : {name: 'hudakchullu'}},"Hello");
  }

  // get List of Activities of School
  getActivity(schoolId) {
    this.studentService.getActivity(schoolId).subscribe((res) => {
      this.allActivities = res;
      this.sportArr = res.filter((e) => (e.fourS == 'Sport'));
      this.serviceArr = res.filter((e) => (e.fourS == 'Service'));
      this.studyArr = res.filter((e) => (e.fourS == 'Study'));
      this.skillArr = res.filter((e) => (e.fourS == 'Skill'));
    },
      (err) => console.log(err)
    );
  }

  // List of enrolled Clubs and Societies
  getEnrolledClub() {
    this.studentService.getAllEnrolledClub().subscribe(res => {
      console.log(res);
      this.enrolledClubsArr = res.filter(e => e.clubOrSociety == "Club");
      this.enrolledSocietyArr = res.filter(e => e.clubOrSociety == "Society");
      this.enrolledClubsArr.forEach(e => this.highlightClubArr.push(e.name));
      this.enrolledSocietyArr.forEach(e => this.highlightSocietyArr.push(e.name));

      console.log(this.highlightClubArr);
      console.log(this.highlightSocietyArr);
    }, (err) => {
      console.log(err);
      this.alertService.showMessageWithSym("You are not member of any Club/Society", "", "info")
    });
  }

  // List of Sessions in current week
  getSessionDetails() {
    this.studentService.getSession("week").subscribe((res) => {
      this.sessionsArr = res.sessions;
    }, (err) => { console.log(err); });
  }

  // List of ALL Grades of a School
  getGrades(schoolId) {
    this.studentService.getGradesOfSchool(schoolId).subscribe((res) => {
      this.grades = res;
    }, (err) => { console.log(err) });
  }

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
    this.clubSchedule = this.filter(Object.assign([], this.copySchedule), val, "Session");
  }

  // Actual Filtering of Sessions on the basis of type
  filter(array: any[], value: string, type: string) {
    let filterSessionArr = [];
    if (type == "Session") {
      if (value)
        filterSessionArr = array.filter(e => e.responses[0].status == value);
      else
        filterSessionArr = array;
    }
    if (type == "Student") {
      if (value)
        filterSessionArr = array.filter(e => e.gradeId == value);
      else
        filterSessionArr = array;
    }

    return filterSessionArr;
  }

  // Details of All Clubs and Societies
  clubDetails(clubObj, type) {
    // Changing color on the basis of 
    this.clubName = clubObj.name;
    //  this.router.navigate(['Student/details/'+clubObj.name],{ state: clubObj });
    this.clubObject = clubObj;
    localStorage.setItem('club', JSON.stringify(clubObj));

    switch (type) {
      case 'sport': this.modalClass = "sportmodal"; break;
      case 'skill': this.modalClass = "skillmodal"; break;
      case 'service': this.modalClass = "servicemodal"; break;
      case 'study': this.modalClass = "studymodal"; break;
    }
    // $('#clubDetailsModal').modal('show');
    this.clubId = clubObj.id;
    this.supervisorId = "";
    this.sessionView = false;
    this.supervisor_loader
      = true;
    this.getCoaches(clubObj.id);
    console.log(clubObj);
  }

  // List of Supervisor of Selected Club/Society
  getCoaches(actiId) {
    this.coaches = [];
    this.students = [];
    this.gradeId = "";
    this.supervisor_loader
      = true;
    this.studentService.getCoach(this.schoolId, actiId).subscribe((res) => {
      this.coaches = res;
      console.log(res);
      this.supervisor_loader
        = false;
    }, (err) => {
      console.log(err);
      this.supervisor_loader
        = false;
    })
  }

  // Session Schedule of a particular Supervisor
  getSupervisorSession(obj_sup) {
    this.getStudents(obj_sup.id);
    this.supervisorName = obj_sup.name;
    this.clubSchedule = []; // reset club schedule
    this.students = [] //reset club+supervisor+student array
    this.sessionView = true; // Session+student view

    this.enrollSch_loader = true; // Supervisor Session loader
    this.supervisorId = obj_sup.id;
    this.studentService.getSupervisorSchedule(this.clubId, obj_sup.id).subscribe((res) => {
      console.log(res.sessions);
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.enrollSch_loader = false;
    }, (err) => {
      console.log(err);
      this.enrollSch_loader = false;
    });
  }

  // List of Student of selected Club/Society under specific Supervisor
  getStudents(supervisorId) {
    this.student_loader
      = true; // Student loader
    this.studentService.getSupervisorStudent(this.clubId, supervisorId).subscribe((res) => {
      this.students = res;
      this.copyStuArr = Object.assign([], res);
      console.log(res);
      this.student_loader
        = false;
    }, (err) => {
      console.log(err);
      this.student_loader
        = false;
    });
  }

  filterStudent(val) {
    this.students = this.filter(Object.assign([], this.copyStuArr), val, "Student");

  }

  // Registration in a particular Club from Club Details Modal
  clubRegBtn() {
    let a = "Activity : " + this.clubName + "\n, Supervisor : " + this.supervisorName;
    this.alertService.confirmWithoutLoader("info", "Are you sure you want to register ?", a, "Yes").then(result => {
      if (result.value) {
        this.clubRegistration();
      }
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
        this.alertService.showSuccessAlert("Request Sent");
        $('#registrationModal').modal('hide');
        $('.modal-backdrop').remove();
        this.clearSelected();
      }, (err) => {
        console.log(err);
        if (err.status === 400) {
          this.alertService.showMessageWithSym("Already applied for the membership of this club and its status is pending or rejected.", "", "info");
        } else {
          this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
        }
      });

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

  enrollmentBtn() {
    this.clubId = "";
  }
}
