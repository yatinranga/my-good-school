import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
declare let $: any;

@Component({
  selector: 'app-teacher-home',
  templateUrl: './teacher-home.component.html',
  styleUrls: ['./teacher-home.component.scss']
})
export class TeacherHomeComponent implements OnInit {

  assignedClubsArr = [];
  assignedSocietyArr = [];
  allAssignedActi = []; // All assigned activities

  createSessionForm: FormGroup; // create session form
  teacherInfo: any;
  schoolId: any;
  teacherId: any;

  schoolGrades = [];
  startTime: any;
  endTime: any;

  sessionsArr = []; // Get Session of a week
  createSessionShow = false;
  editSessionShow = false;
  teacherName: any;

  createSessionView = true;
  editSessionView = false;

  // Club details variable
  clubId: any;
  clubName = "";
  filterVal = "";
  filterReqVal = "";
  clubSchedule = [];
  copySchedule = [];
  studentsArr = [];
  copyStudentArr = [];
  clubReqArr = [];
  copyClubReqArr = [];
  clubSch_loader = false;
  stu_loader = false;
  clubReqLoader = false;
  gradeId = "";


  constructor(private teacherService: TeacherService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.teacherName = this.teacherInfo.teacher.name;
    this.teacherId = this.teacherInfo.teacher.id;
    this.clubReqLoader = true;
    // this.getAllClubReq();
    this.getAllClubs();
    this.getSessionDetails();

    this.createSessionForm = this.formBuilder.group({
      id : [null],
      number: [, [Validators.required]],
      startDate: [, [Validators.required]],
      endDate: [],
      title: [, [Validators.required]],
      clubId: [, [Validators.required]],
      gradeIds: [, [Validators.required]],
    });

    this.getSchoolGrades(this.schoolId);
  }

  //get list of assigned/supervised Clubs and Society
  getAllClubs() {
    this.teacherService.getAssignedClubs().subscribe(res => {
      this.assignedClubsArr = res.filter((e) => (e.clubOrSociety == "Club"));
      this.assignedSocietyArr = res.filter((e) => (e.clubOrSociety == "Society"));
      this.allAssignedActi = res;
    }, (err) => { console.log(err); });
  }

  // get all the Request of Clubs and Society
  // getAllClubReq() {
  //   this.teacherService.getClubReq().subscribe(res => {
  //     this.clubReqArr = res;
  //     this.clubReqLoader = false;
  //   }, (err) => {
  //     console.log(err);
  //     this.clubReqLoader = false;
  //   });
  // }

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
            this.getClubRequests(this.clubId);
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
            this.getClubRequests(this.clubId);

          }, (err) => { console.log(err) })
        }
      })
    }

  }

  // get List of School Grades 
  getSchoolGrades(schoolId) {
    this.teacherService.getGrades(schoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
      (err) => console.log(err));
  }

  // Create Session
  createSession() {
    console.log("create session");
    this.createSessionForm.value.endDate = this.createSessionForm.value.startDate + " " + this.endTime + ":00";
    this.createSessionForm.value.startDate = this.createSessionForm.value.startDate + " " + this.startTime + ":00";
    console.log(this.createSessionForm.value);

    this.alertService.showLoader("");

    if(this.createSessionView){
      this.teacherService.createNewSession(this.createSessionForm.value).subscribe((res) => {
        console.log(res);
        $('#createSessionModal').modal('hide');
        $('.modal-backdrop').remove();
        this.alertService.showMessageWithSym("Session Created !", "Success", "success");
        this.resetForm();
        this.getSessionDetails();
      }, (err) => {
        console.log(err);
      });
    }

    if(this.editSessionView){
      this.teacherService.editSession(this.createSessionForm.value).subscribe((res) => {
        console.log(res);
        $('#createSessionModal').modal('hide');
        $('.modal-backdrop').remove();
        this.alertService.showMessageWithSym("Session Edited !", "Success", "success");
        this.resetForm();
        this.getSessionDetails();
      }, (err) => {
        console.log(err);
      });
    }
  }

  // Reset Form
  resetForm() {
    this.startTime = "";
    this.endTime = "";
    this.createSessionForm.reset();
  }

  // Sorting on the basis of Status
  // order: boolean = false;
  // sortByStatus() {
  //   this.order = !this.order;
  //   // sort by activityStatus
  //   this.clubReqArr.sort((a, b) => {
  //     const nameA = a.membershipStatus.toUpperCase(); // ignore upper and lowercase
  //     const nameB = b.membershipStatus.toUpperCase(); // ignore upper and lowercase
  //     if (nameA < nameB) {
  //       return this.order ? -1 : 1;
  //     }
  //     if (nameA > nameB) {
  //       return this.order ? 1 : -1;
  //     }

  //     // names must be equal
  //     return 0;
  //   });
  // }

  // List of Sessions in current week
  getSessionDetails() {
    this.teacherService.getSession("week").subscribe((res) => {
      console.log(res.sessions);
      this.sessionsArr = res.sessions;
    }, (err) => { console.log(err); });
  }

  // Delete Scheduled Session
  deleteSession(session, out_index, in_index) {
    console.log(session);
    this.alertService.confirmWithoutLoader('question', 'Sure you want to DELETE ?', '', 'Yes').then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        this.teacherService.deleteSession(session.id).subscribe((res) => {
          console.log(res);
          this.sessionsArr[out_index].responses.splice(in_index, 1);
          this.alertService.showMessageWithSym("Session Deleted", "Success", "success");
        }, (err) => { console.log(err) });
      }

    });
  }

  createSessionBtn() {
    this.createSessionView = true;
    this.editSessionView = false;
  }

  // Edit Current Session
  editSessionBtn(session,i,j) {
    $('#createSessionModal').modal('show');
    this.createSessionView = false;
    this.editSessionView = true;
    console.log(session);
    this.editSessionShow = true;
    let sDate = new Date(session.startDate);
    let eDate = new Date(session.endDate);

    if (sDate.getHours() < 10) {
      if (sDate.getMinutes() == 0)
        this.startTime = "0" + sDate.getHours() + ":" + sDate.getMinutes() + "0";
      else
        this.startTime = "0" + sDate.getHours() + ":" + sDate.getMinutes();
    }
    else {
      if (sDate.getMinutes() == 0)
        this.startTime = sDate.getHours() + ":" + sDate.getMinutes() + "0";
      else
        this.startTime = sDate.getHours() + ":" + sDate.getMinutes();
    }

    if (eDate.getHours() < 10) {
      if (eDate.getMinutes() == 0)
        this.endTime = "0" + eDate.getHours() + ":" + eDate.getMinutes() + "0";
      else
        this.endTime = "0" + eDate.getHours() + ":" + eDate.getMinutes();
    }
    else {
      if (eDate.getMinutes() == 0)
        this.endTime = eDate.getHours() + ":" + eDate.getMinutes() + "0";
      else
        this.endTime = eDate.getHours() + ":" + eDate.getMinutes();
    }

    this.createSessionForm.controls.startDate.patchValue(session.startDate.split(' ')[0]);
    this.createSessionForm.patchValue({
      id: session.id,
      number: session.number,
      title: session.title,
      clubId: session.club.id
    });

    let arr = [];
    session.grades.forEach(element => {
      arr.push(element.id);
      this.createSessionForm.controls.gradeIds.setValue(arr);
    });
  }

  // Details of All Clubs and Societies
  clubDetails(clubObj) {
    $('#clubDetailsModal').modal('show');
    this.clubSchedule = [];
    this.studentsArr = [];
    this.clubReqArr = [];
    this.filterVal = ""; // Reset Filter value
    this.filterReqVal = "";
    this.gradeId = "";
    this.clubSch_loader = true;
    this.stu_loader = true;
    this.clubReqLoader = true;
    this.clubName = clubObj.name;
    this.clubId = clubObj.id;
    this.getClubRequests(clubObj.id);
    this.getClubStudents(clubObj.id, this.teacherId);
    this.getClubSession(clubObj.id);
    this.createSessionForm.controls.clubId.patchValue(this.clubId);
  }

  // get Session of a particualr Club/Society
  getClubSession(clubId) {
    this.teacherService.getSupervisedClubSession(clubId).subscribe((res) => {
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.clubSch_loader = false;
    }, (err) => {
      console.log(err);
      this.clubSch_loader = false;
    });
  }

  // Filter session on the bases of ALL, UPCOMING and ENDED
  filterSession(val) {
    this.clubSchedule = this.filter(Object.assign([], this.copySchedule), val, "Session");
  }

  // List of Students of the particular Club
  getClubStudents(clubId, teacherId) {
    this.teacherService.getSupervisorClubStudents(clubId, teacherId).subscribe((res) => {
      this.studentsArr = res;
      this.copyStudentArr = Object.assign([], res);
      this.stu_loader = false;
    }, (err) => {
      console.log(err);
      this.stu_loader = false;
    });

  }

  // Filter student on the bases of grade
  filterStudent(val) {
    this.studentsArr = this.filter(Object.assign([], this.copyStudentArr), val, "Student");
  }

  // Requests of a particular club/society
  getClubRequests(clubId){
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

  filterRequests(val){
    this.clubReqArr = this.filter(Object.assign([], this.copyClubReqArr), val, "Request");
  }

  newSessionBtn(){
    
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

