import { Component, OnInit, Input } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { BASE_URL } from 'src/app/services/app.constant';


@Component({
  selector: 'app-coordinator-club-details',
  templateUrl: './coordinator-club-details.component.html',
  styleUrls: ['./coordinator-club-details.component.scss']
})
export class CoordinatorClubDetailsComponent implements OnInit {
  BASE_URL: string;

  @Input() clubObject: any;
  userInfo:any;
  supervisorArr = [];
  studentsArr = [];
  clubSchedule = [];
  copySchedule = [];
  copyStuArr = [];
  grades = [];
  supervisorId: any;
  filterVal = "";
  gradeId = "";
  sup_loader: boolean = false;
  stu_loader: boolean = false;
  session_loader: boolean = false;
  constructor(private teacherService: TeacherService) {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
   }

  ngOnInit() {
    this.userInfo = JSON.parse(localStorage.getItem('user_type'));
    this.getGrades(this.userInfo.schoolId);
  }

  ngOnChanges(clubObject: any) {
    this.getClubSupervisor();
    this.filterVal = "";
    this.gradeId = "";
  }

  getClubSupervisor() {
    this.supervisorArr = [];
    this.sup_loader = true;
    this.teacherService.getClubSupervisor(this.clubObject.id).subscribe(res => {
      this.supervisorArr = res;
      this.sup_loader = false;
    }, (err => {
      console.log(err);
      this.sup_loader = false;
    }))
  }

  getSupervisorSession(supervisor_obj) {
    this.session_loader = true;
    this.supervisorId = supervisor_obj.id;
    // this.supervisorName = supervisor_obj.name;
    this.getStudents(supervisor_obj.id);

    this.clubSchedule = [];
    this.teacherService.getSupervisorSchedule(this.clubObject.id, supervisor_obj.id).subscribe((res) => {
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.session_loader = false;
    }, (err) => {
      console.log(err);
      this.session_loader = false;
    });
  }

  // List of Student of selected Club/Society under specific Supervisor
  getStudents(supervisorId) {
    this.studentsArr = [];
    this.stu_loader = true; // Student loader
    this.teacherService.getSupervisorClubStudents(this.clubObject.id, supervisorId).subscribe((res) => {
      this.studentsArr = res;
      this.copyStuArr = Object.assign([], res);
      this.stu_loader = false;
    }, (err) => {
      console.log(err);
      this.stu_loader = false;
    });
  }

  // List of ALL Grades of a School
  getGrades(schoolId) {
    this.teacherService.getGrades(schoolId).subscribe((res) => {
      this.grades = res;
    }, (err) => { console.log(err) });
  }

  // Filter session on the bases of ALL, UPCOMING and ENDED
  filterSession(val) {
    this.clubSchedule = this.filter(Object.assign([], this.copySchedule), val, "Session");
  }

  filterStudent(val) {
    this.studentsArr = this.filter(Object.assign([], this.copyStuArr), val, "Student");
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

}
