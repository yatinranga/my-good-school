import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-teacher-home',
  templateUrl: './teacher-home.component.html',
  styleUrls: ['./teacher-home.component.scss']
})
export class TeacherHomeComponent implements OnInit {

  clubReqArr = [];
  clubReqLoader = false;

  constructor(private teacherService: TeacherService, private alertService: AlertService) { }

  ngOnInit() {
    this.clubReqLoader = true;
    this.teacherService.getClubReq().subscribe(res => {
      console.log(res);
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
            console.log(res);
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
            console.log(res);
            this.alertService.showSuccessAlert("Request Rejected!");
            this.clubReqArr.splice(index, 1);
            this.clubReqArr.unshift(res);
          }, (err) => { console.log(err) })
        }
      })
    }
    
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
