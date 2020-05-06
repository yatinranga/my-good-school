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

  clubReqArr = [];
  assignedClubsArr = [];
  assignedSocietyArr = [];
  allAssignedActi = [];
  clubReqLoader = false;

  createSessionForm: FormGroup; // create session form
  teacherInfo: any;
  schoolId: any;
  adminService: any;
  schoolGrades: any;
  startTime: any;
  endTime: any;

  sessionsArr = []; // Get Session of a week
  createSessionShow = false;
  editSessionShow = false;
  teacherName: any;

  // Club details variable
  clubName = "";
  filterVal = "";
  clubSchedule = [];
  copySchedule = [];
  clubSch_loader = false;


  constructor(private teacherService: TeacherService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.teacherName = this.teacherInfo.teacher.name;
    this.clubReqLoader = true;
    this.getAllClubReq();
    this.getAllClubs();
    this.getSessionDetails();

    this.createSessionForm = this.formBuilder.group({
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
  getAllClubReq() {
    this.teacherService.getClubReq().subscribe(res => {
      this.clubReqArr = res;
      this.clubReqLoader = false;
    }, (err) => {
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
            this.clubReqArr.unshift(res);
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
            this.clubReqArr.unshift(res);
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
    this.teacherService.createNewSession(this.createSessionForm.value).subscribe((res) => {
      console.log(res);
      $('#createSessionModal').modal('hide');
      $('.modal-backdrop').remove();
      this.alertService.showMessageWithSym("Session Created !","Success","success");
      this.resetForm();
    }, (err) => { console.log(err);
     });
  }

  // Reset Form
  resetForm() {
    this.startTime = "";
    this.endTime = "";
    this.createSessionForm.reset();
  }

  // Sorting on the basis of Status
  order: boolean = false;
  sortByStatus() {
    this.order = !this.order;
    // sort by activityStatus
    this.clubReqArr.sort((a, b) => {
      const nameA = a.membershipStatus.toUpperCase(); // ignore upper and lowercase
      const nameB = b.membershipStatus.toUpperCase(); // ignore upper and lowercase
      if (nameA < nameB) {
        return this.order ? -1 : 1;
      }
      if (nameA > nameB) {
        return this.order ? 1 : -1;
      }

      // names must be equal
      return 0;
    });
  }

  // List of Sessions in current week
  getSessionDetails() {
    this.teacherService.getSession("week").subscribe((res) => {
      console.log(res.sessions);
      this.sessionsArr = res.sessions;
    }, (err) => { console.log(err); });
  }

  // Delete Scheduled Session
  deleteSession(session, index) {
    console.log(session);
    this.alertService.confirmWithoutLoader('question', 'Sure you want to DELETE ?', '', 'Yes').then(result => {
      if (result.value) {       
        this.alertService.showLoader("");
        this.teacherService.deleteSession(session.id).subscribe((res)=> {
          console.log(res);
          this.sessionsArr.splice(index,1);
          this.alertService.showMessageWithSym("Session Deleted","Success","success");
        },(err)=>{console.log(err)});
      }

    });
  }

  createSessionBtn() {
    console.log("Create Session btn");
    this.createSessionShow = true;
    this.startTime = "";
    this.endTime = "";
    this.createSessionForm.reset();
  }

  // Edit Current Session
  editSessionBtn(session,index){
    $('#createSessionModal').modal('show');
    console.log(session);
    this.editSessionShow = true;
    let sDate = new Date(session.startDate);
    let eDate = new Date(session.endDate);

    if(sDate.getHours()<10){
      if(sDate.getMinutes()==0)
        this.startTime = "0"+sDate.getHours()+":"+sDate.getMinutes()+"0";
      else
        this.startTime = "0"+sDate.getHours()+":"+sDate.getMinutes();
    } 
    else {
      if(sDate.getMinutes()==0)
        this.startTime = sDate.getHours()+":"+sDate.getMinutes()+"0";
      else
        this.startTime = sDate.getHours()+":"+sDate.getMinutes();
    }

    if(eDate.getHours()<10){
      if(eDate.getMinutes()==0)
        this.endTime = "0"+eDate.getHours()+":"+eDate.getMinutes()+"0";
      else
        this.endTime = "0"+eDate.getHours()+":"+eDate.getMinutes();
    }
    else {
      if(eDate.getMinutes()==0)
        this.endTime = eDate.getHours()+":"+eDate.getMinutes()+"0";
      else
        this.endTime = eDate.getHours()+":"+eDate.getMinutes();
    }

    this.createSessionForm.controls.startDate.patchValue(session.startDate.split(' ')[0]);   
    this.createSessionForm.patchValue({
      number: session.number,
      title: session.title,
      clubId: session.club.id
      // gradeIds: [, [Validators.required]],

    });
    let arr = [];
    session.grades.forEach(element => {
        // console.log(element.id);
        arr.push(element.id);
        this.createSessionForm.controls.gradeIds.setValue(arr);     
      });
  }

  // Details of All Clubs and Societies
   clubDetails(clubObj) {
    $('#clubDetailsModal').modal('show');
    this.clubSchedule = [];
    this.filterVal = ""; // Reset Filter value
    this.clubSch_loader = true;
    this.clubName = clubObj.name;
    this.getClubSession(clubObj.id)
   }

   // get Session of a particualr Club/Society
   getClubSession(clubId){
     this.teacherService.getSupervisedClubSession(clubId).subscribe((res)=>{
       console.log(res.sessions);
       this.clubSchedule = res.sessions;
       this.copySchedule = Object.assign([],res.sessions);
       this.clubSch_loader = false;
     },(err)=>{
      console.log(err);
      this.clubSch_loader = false;});
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

