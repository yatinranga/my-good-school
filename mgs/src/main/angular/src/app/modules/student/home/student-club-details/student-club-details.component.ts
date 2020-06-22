import { Component, OnInit, Input } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-activity-details',
  templateUrl: './student-club-details.component.html',
  styleUrls: ['./student-club-details.component.scss']
})
export class StudentClubDetailsComponent implements OnInit {

  @Input() clubObject: any;
  studentInfo: any;
  modalClass = "";
  schoolId = "";
  supervisorId = "";
  grades = [];
  gradeId = "";
  filterVal = "";
  supervisorName = "";

  coaches = [];
  studentsArr = [];
  copyStuArr = [];
  clubSchedule = [];
  copySchedule = [];
  stu_loader = false;
  sup_loader = true;
  enrollSch_loader = false;
  constructor(private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {

    /**If Object is present in localStorage, move it to sessionStorage and clear object from localStorage */
    // if (localStorage.getItem('club')) {
    //   sessionStorage.setItem('club', JSON.stringify(JSON.parse(localStorage.getItem('club'))));
    //   localStorage.removeItem('club');
    //   this.clubObject = JSON.parse(sessionStorage.getItem('club'));
    // } else {
    //   this.clubObject = JSON.parse(sessionStorage.getItem('club'));
    // }
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.schoolId;
    this.getGrades(this.schoolId);
  }

  ngOnChanges(clubObject: any) {
    this.setClass(this.clubObject.fourS);
    this.getCoaches(this.clubObject.id);
    console.log(this.clubObject);
    this.studentsArr = [];
    this.clubSchedule = [];
    this.supervisorId = "";
    this.gradeId = "";
    this.filterVal = "";
  }

  setClass(club_type) {
    switch (club_type) {
      case 'Sport': this.modalClass = "sportmodal"; break;
      case 'Skill': this.modalClass = "skillmodal"; break;
      case 'Service': this.modalClass = "servicemodal"; break;
      case 'Study': this.modalClass = "studymodal"; break;
    }
  }

  getCoaches(actiId) {
    this.coaches = [];
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

  getSupervisorSession(supervisor_obj) {
    console.log(supervisor_obj);
    this.enrollSch_loader = true;
    this.supervisorId = supervisor_obj.id;
    this.supervisorName = supervisor_obj.name;
    this.getStudents(supervisor_obj.id);

    this.clubSchedule = [];
    this.studentService.getSupervisorSchedule(this.clubObject.id, supervisor_obj.id).subscribe((res) => {
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
    this.studentsArr = [];
    this.stu_loader = true; // Student loader
    this.studentService.getSupervisorStudent(this.clubObject.id, supervisorId).subscribe((res) => {
      this.studentsArr = res;
      this.copyStuArr = Object.assign([], res);
      console.log(res);
      this.stu_loader = false;
    }, (err) => {
      console.log(err);
      this.stu_loader = false;
    });
  }

  filterStudent(val) {
    this.studentsArr = this.filter(Object.assign([], this.copyStuArr), val, "Student");
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

  clubRegBtn() {
    let a = "Activity : " + this.clubObject.name + "\n, Supervisor : " + this.supervisorName;
    this.alertService.confirmWithoutLoader("info", "Are you sure you want to register ?", a, "Yes").then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        this.studentService.postEnrollInClub(this.clubObject.id, this.supervisorId).subscribe(res => {
          console.log(res)
          this.alertService.showSuccessAlert("Request Sent");
        }, (err) => {
          console.log(err);
          if (err.status === 400) {
            this.alertService.showMessageWithSym("Already applied for the membership of this club and its status is pending or rejected.", "", "info");
          }
          else {
            this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
          }
        });
      }
    })

  }

}
