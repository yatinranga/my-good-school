import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-coordinator-awards',
  templateUrl: './coordinator-awards.component.html',
  styleUrls: ['./coordinator-awards.component.scss']
})
export class CoordinatorAwardsComponent implements OnInit {

  userType:any;
  awardsArr = [];
  award_loader: boolean = false;

  constructor(private teacherService: TeacherService, private alertService: AlertService) { }

  ngOnInit() {
    this.userType = JSON.parse(localStorage.getItem('user_type'));
    this.getAwards();
  }

  /** Get List of Awards */
  getAwards() {
    this.award_loader = true;
    this.teacherService.getCoordinatorAwards().subscribe(res => {
      this.awardsArr = res;
      this.award_loader = false;
    }, (err => {
      this.award_loader = false;
      console.log(err);
    }))
  }

  /** Verify the Selected Award*/
  verifySelectedAward(e, i, val: boolean) {
    e.stopPropagation();
    const awardId = this.awardsArr[i].id;
    console.log(awardId);

    if (val) {
      this.alertService.confirmWithoutLoader('question', "Review Award", '', 'Yes').then(result => {
        if (result.value) {
          this.alertService.showLoader("");
          this.teacherService.verifyAwards(awardId,"true").subscribe((res) => {
            this.alertService.showSuccessAlert("");
            this.awardsArr[i].status = res.status;
          },
            (err) => {
              console.log(err);
              this.errorMessage(err);
            });
        }
      })
    } else {
      this.alertService.confirmWithoutLoader('question', "Reject Award", '', 'Yes').then(result => {
        if (result.value) {
          this.alertService.showLoader("");
          this.teacherService.verifyAwards(awardId,"false").subscribe((res) => {
            this.alertService.showSuccessAlert("");
            this.awardsArr[i].status = "REJECTED";
          },
            (err) => {
              console.log(err);
              this.errorMessage(err);
            });
        }
      })
    }
  }

  // Sort the given Awards List
  order: boolean = false;
  sortByStatus() {
    this.order = !this.order;
    // sort by activityStatus
    this.awardsArr.sort((a, b) => {
      const nameA = a.status.toUpperCase(); // ignore upper and lowercase
      const nameB = b.status.toUpperCase(); // ignore upper and lowercase
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

  // stop toogle of table
  stopCollapse(e) {
    e.stopPropagation();
  }

  /** Handling Error */
  errorMessage(err) {
    if (err.status == 400) {
      this.alertService.showMessageWithSym(err.msg, "", "info");
    }
    else {
      this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
    }
  }


  countStar(count){
    const array = [];
    var mode = count;
    for (var index = 0; index < Math.floor(count); index++) {
      array[index] = index+1;
    }
    if(mode.toString().split('.')[1]) array[index] = parseFloat('0.'+ mode.toString().split('.')[1]);
    return array;
  }

  star(star){
    switch (star) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
        return 'fa fa-star'
        break;
        case 0.5:
          return 'fa fa-star-half'
        break;
    }
  }

}
