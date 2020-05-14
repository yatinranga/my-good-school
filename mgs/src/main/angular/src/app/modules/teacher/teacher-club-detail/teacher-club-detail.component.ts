import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-teacher-club-detail',
  templateUrl: './teacher-club-detail.component.html',
  styleUrls: ['./teacher-club-detail.component.scss']
})
export class TeacherClubDetailComponent implements OnInit {
  clubObject: any;
  teacherInfo: any;

  clubReqArr = [];
  copyClubReqArr = [];
  studentsArr = [];
  copyStudentArr = [];
  schoolGrades = [];
  clubSchedule = [];
  copySchedule = [];

  stu_loader = false;
  clubSch_loader = false;
  clubReqLoader = false;

  filterReqVal = "";
  gradeId = "";
  filterVal = "";

  constructor(private teacherService: TeacherService, private alertService: AlertService) { }

  ngOnInit() {
    this.clubObject = JSON.parse(localStorage.getItem('club'));
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getSchoolGrades(this.teacherInfo.teacher.schoolId);
    this.getClubRequests(this.clubObject.id);
    this.getClubStudents(this.clubObject.id,this.teacherInfo.teacher.id);
    this.getClubSession(this.clubObject.id);
  }

    // get List of School Grades 
    getSchoolGrades(schoolId) {
      this.teacherService.getGrades(schoolId).subscribe((res) => {
        this.schoolGrades = res;
      },
        (err) => console.log(err));
    }

  // Requests of a particular club/society
  getClubRequests(clubId){
    this.clubReqLoader = true;
    this.teacherService.getSupervisorClubReq(clubId).subscribe((res)=>{
      console.log(res);
      this.clubReqArr = res;
      this.copyClubReqArr = Object.assign([], res);
    this.clubReqLoader = false;
    },(err)=>{
      console.log(err);
    this.clubReqLoader = false;
    });
  }

  setClubReq(obj, index, verify) {

    // To Verify/Approve the Club/Society Request
    if (verify == 'true') {
      this.alertService.confirmWithoutLoader('question', 'Sure you want to Approve ?', '', 'Yes').then(result => {
        if (result.value) {
          this.alertService.showLoader("");
          this.teacherService.approveClubReq(obj.student.id, obj.club.id, verify).subscribe(res => {
            this.alertService.showSuccessAlert("Request Approved!");
            this.clubReqArr.splice(index, 1);
            // this.clubReqArr.unshift(res);
            this.getClubRequests(this.clubObject.id);
            this.getClubStudents(this.clubObject.id, this.teacherInfo.teacher.id);
          }, (err) => { console.log(err); })
        }
      })
    }

    // To Reject the Club/Society Request
    if (verify == 'false') {
      this.alertService.confirmWithoutLoader('question', 'Sure you want to Reject ?', '', 'Yes').then(result => {
        if (result.value) {
          this.alertService.showLoader("");
          this.teacherService.approveClubReq(obj.student.id, obj.club.id, verify).subscribe(res => {
            this.alertService.showSuccessAlert("Request Rejected!");
            this.clubReqArr.splice(index, 1);
            // this.clubReqArr.unshift(res);
            this.getClubRequests(this.clubObject.id);

          }, (err) => { console.log(err) })
        }
      })
    }

  }

  filterRequests(val){
    this.clubReqArr = this.filter(Object.assign([], this.copyClubReqArr), val, "Request");
  }

    // Filter student on the bases of grade
    filterStudent(val) {
      this.studentsArr = this.filter(Object.assign([], this.copyStudentArr), val, "Student");
    }

    // Filter session on the bases of ALL, UPCOMING and ENDED
    filterSession(val) {
      this.clubSchedule = this.filter(Object.assign([], this.copySchedule), val, "Session");
    }

    // List of Students of the particular Club
    getClubStudents(clubId, teacherId) {
      this.stu_loader = true;
      this.teacherService.getSupervisorClubStudents(clubId, teacherId).subscribe((res) => {
        this.studentsArr = res;
        this.copyStudentArr = Object.assign([], res);
        this.stu_loader = false;
      }, (err) => {
        console.log(err);
        this.stu_loader = false;
      });
  
    }

      // get Session of a particualr Club/Society
  getClubSession(clubId) {
    this.clubSch_loader = true;
    this.teacherService.getSupervisedClubSession(clubId).subscribe((res) => {
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.clubSch_loader = false;
    }, (err) => {
      console.log(err);
      this.clubSch_loader = false;
    });
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
    if (type == "Request") {
      if (value)
        filterSessionArr = array.filter(e => e.membershipStatus == value);
      else
        filterSessionArr = array;
    }

    return filterSessionArr;
  }

}
