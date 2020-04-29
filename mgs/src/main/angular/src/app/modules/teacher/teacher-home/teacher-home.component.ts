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


  constructor(private teacherService: TeacherService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.clubReqLoader = true;
    this.getAllClubReq();
    this.getAllClubs();

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
    this.createSessionForm.value.endDate = this.createSessionForm.value.startDate + " " + this.endTime + ":00";
    this.createSessionForm.value.startDate = this.createSessionForm.value.startDate + " " + this.startTime + ":00";
    console.log(this.createSessionForm.value);
    
    this.teacherService.createNewSession(this.createSessionForm.value).subscribe((res) => {
      console.log(res);
      $('#addActivityModal').modal('hide');
      $('.modal-backdrop').remove();
      this.alertService.showErrorAlert("Session Created !");
    }, (err) => { console.log(err); })


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

}
