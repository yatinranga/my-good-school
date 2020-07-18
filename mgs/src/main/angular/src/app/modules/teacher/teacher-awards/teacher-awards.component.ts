import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';

declare let $: any;

@Component({
  selector: 'app-teacher-awards',
  templateUrl: './teacher-awards.component.html',
  styleUrls: ['./teacher-awards.component.scss']
})
export class TeacherAwardsComponent implements OnInit {

  awardsList = [];
  schoolGrades = [];
  performedActiArr = [];
  schoolAwards = []; // List of all Awards of a school

  awardViewType = "view";
  teacherInfo: any;
  schoolId = "";
  activities = [];
  studentActivityList = false;
  actiPerform = {};
  studentId = "";

  activityId = ""
  teacherId = "";

  award_loader: boolean;
  pa_loader: boolean; // Performed Activities Details Loader

  assignAwardForm: FormGroup;

  //New Changes
  awardCriteriaArr: any = [];
  criteriaValuesArr: any = [];

  awardcr_loader: boolean;
  criteriaVal_loader: boolean;

  criterionValue: any = "";
  awardCriterion: any = "";
  gradeId: any = "";

  showCriteriaValues = true;
  endDate: string;
  startDate: string;
  count = 0; //to count the number of words enter

  constructor(private teacherService: TeacherService, private formbuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo.schoolId;
    this.teacherId = this.teacherInfo.id;
    this.assignAwardInit();
    this.getSchoolGrades();
    this.awardView(this.awardViewType);
    this.getSchoolActivities();
  }

  // Initialize the Assign Award Form
  assignAwardInit() {
    this.assignAwardForm = this.formbuilder.group({
      teacherId: [],
      schoolId: [],
      awardCriterion: [],
      criterionValue: [],
      validFrom: [],
      validUntil: [],
      gradeId: [null],
      awardType: [, [Validators.required]],
      description: [, [Validators.required, Validators.minLength(10)]],
      activityPerformedIds: [([])],
    });
  }

  // get ALL Activites Offered in a School
  getSchoolActivities() {
    this.teacherService.getActivity(this.schoolId).subscribe((res) => {
      // this.activities = res;
      res.forEach((element) => { this.activities.push(element.name) });
    }, (err) => { console.log(err) });
  }

  // get Grades of School
  getSchoolGrades() {
    this.teacherService.getGrades(this.schoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
      (err) => console.log(err));
  }

  // Assign Award to Students
  assignAward() {

    this.pa_loader = true;
    this.assignAwardForm.value.teacherId = this.teacherId;
    this.assignAwardForm.value.schoolId = this.schoolId;
    this.assignAwardForm.value.studentId = this.studentId;
    this.assignAwardForm.value.activityPerformedIds = [];
    this.assignAwardForm.value.awardCriterion = this.awardCriterion;
    this.assignAwardForm.value.criterionValue = this.criterionValue;
    this.assignAwardForm.value.validFrom = this.startDate + " 00:00:00";
    this.assignAwardForm.value.validUntil = this.endDate + " 00:00:00";
    this.assignAwardForm.value.gradeId = null;
    Object.keys(this.actiPerform).forEach((key) => {
      if (this.actiPerform[key]) {
        this.assignAwardForm.value.activityPerformedIds.push(key);
      }
    })

    console.log(this.assignAwardForm.value);
    let validFrom = this.startDate + " 00:00:00";
    let validUntil = this.endDate + " 00:00:00";

    this.teacherService.assignAward(this.assignAwardForm.value).subscribe((res) => {
      console.log(res);
      this.alertService.showSuccessAlert("");
      $('#assignAwardModal').modal('hide');
      $('.modal-backdrop').remove();
      this.performedActiArr = [];
      this.assignAwardForm.reset();
      this.studentActivityList = false;
      this.awardViewType = "view";
      this.pa_loader = false;
      this.viewAwards();
    },
      (err) => {
        console.log(err);
        if (err.status == 400) {
          this.alertService.showMessageWithSym(err.msg, "", "info");
        }
        else {
          this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
        }
        // $('#assignAwardModal').modal('hide');
        // $('.modal-backdrop').remove();
        this.pa_loader = false;
      });
  }

  // Awards between the dates
  dateChanged() {
    this.pa_loader = true;
    let sDate = this.startDate + ' 00:00:00';
    let eDate = this.endDate + ' 00:00:00';
    this.performedActiArr = [];
    if (this.gradeId) {
      this.teacherService.getStudentPerformedActivities(this.awardCriterion, this.criterionValue, sDate, eDate, this.gradeId).subscribe((res) => {
        console.log(res);
        this.performedActiArr = res;
        this.pa_loader = false;
      },
        (err) => {
          console.log(err);
          this.pa_loader = false;
        });

    } else {
      this.teacherService.getStudentPerformedActivities(this.awardCriterion, this.criterionValue, sDate, eDate).subscribe((res) => {
        this.performedActiArr = res;
        console.log(res);
        this.pa_loader = false;
      },
        (err) => {
          console.log(err)
          this.pa_loader = false;
        });

    }
  }

  // get List of Performed activity by specific activityId
  getPerformedActivities(type, value?) {
    this.pa_loader = true;
    this.studentActivityList = true;
    // console.log(this.criterionValue)
    this.performedActiArr = [];
    let sDate = this.startDate + ' 00:00:00';
    let eDate = this.endDate + ' 00:00:00';
    if (type == "value") {
      if (this.gradeId == "") {
        this.criterionValue = value;
        this.teacherService.getStudentPerformedActivities(this.awardCriterion, value, sDate, eDate).subscribe((res) => {
          this.performedActiArr = res;
          console.log(res);
          this.pa_loader = false;
          // this.pa_loader = false;
        },
          (err) => {
            console.log(err)
            this.pa_loader = false;
          });
      } else {
        this.criterionValue = value;
        this.teacherService.getStudentPerformedActivities(this.awardCriterion, value, sDate, eDate, this.gradeId).subscribe((res) => {
          console.log(res);
          this.performedActiArr = res;
          this.pa_loader = false;
          // this.pa_loader = false;
        },
          (err) => {
            console.log(err);
            this.pa_loader = false;
          });
      }
    }

    if (type == "grade") {
      this.gradeId = value;
      if (this.criterionValue) {
        this.teacherService.getStudentPerformedActivities(this.awardCriterion, this.criterionValue, sDate, eDate, value).subscribe((res) => {
          console.log(res);
          this.performedActiArr = res;
        }, (err) => {
          console.log(err);
          this.pa_loader = false;
        });
      }
    }
  }

  // change award view
  awardView(type) {
    this.awardViewType = type;
    console.log("Award View Type = " + this.awardViewType);
    this.viewAwards();
    if (type == "assign") {
      this.awardCriteria();
      this.performedActiArr = [];
      this.studentActivityList = false;
      this.showCriteriaValues = true;
      this.setEndDate();
      this.setStartDate();
    }
  }

  // To view Awards
  viewAwards() {
    if (this.awardViewType == "view") {

      this.criterionValue = ""; // Reset the value of Criteria dropdown
      this.awardCriterion = ""; // Reset the value of Criteria Values dropdown
      this.gradeId = ""; // Reset the value of Grades dropdown

      this.award_loader = true;
      this.teacherService.getTeacherAwards().subscribe((res) => {
        this.awardsList = res;
        console.log(this.awardsList);
        this.award_loader = false;
      },
        (err) => {
          console.log(err)
          this.award_loader = false;
        });
    }
  }

  // to get all awards of school
  getSchoolAwards() {
    console.log(this.gradeId);
    this.award_loader = true;
    this.schoolAwards = [];
    this.assignAwardForm.value.activityPerformedIds = [];
    Object.keys(this.actiPerform).forEach((key) => {
      if (this.actiPerform[key]) {
        this.assignAwardForm.value.activityPerformedIds.push(key);
      }
    })

    if (this.assignAwardForm.value.activityPerformedIds.length > 0) {
      $('#assignAwardModal').modal('show');
      $('#assignAwardModal').modal({
        backdrop: 'static',
        keyboard: false
      });

      this.teacherService.getAwards().subscribe((res) => {
        this.schoolAwards = res;
        this.award_loader = false;
      },
        (err) => {
          console.log(err);
          this.award_loader = false;
        });

    } else {
      this.alertService.showErrorAlert("Please select atleast one activity");
    }
  }


  // Verify the Selected Award 
  verifySelectedAward(e, i) {
    e.stopPropagation();
    const awardId = this.awardsList[i].id;
    console.log(awardId);

    this.alertService.confirmWithoutLoader('question', "Verify Actvity", '', 'Yes').then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        this.teacherService.verifyAwards(awardId).subscribe((res) => {
          console.log(res);
          this.alertService.showSuccessAlert("");
          this.awardsList[i].status = "VERIFIED";
        },
          (err) => {
            console.log(err);
          });
      }
    })
  }

  // stop toogle of table
  stopCollapse(e) {
    e.stopPropagation();
  }

  // Sort the given Awards List
  order: boolean = false;
  sortByStatus() {
    this.order = !this.order;
    // sort by activityStatus
    this.awardsList.sort((a, b) => {
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

  // Reset Award Details Form
  resetForm() {
    this.assignAwardForm.reset();
  }

  // get Award Criteria
  awardCriteria() {
    this.awardcr_loader = true;
    this.teacherService.getAwardCriteria().subscribe((res) => {
      this.awardcr_loader = false;
      this.awardCriteriaArr = res;
    }, (err) => {
      console.log(err);
      this.awardcr_loader = false;
    });
  }


  // get Values on the basis of Award Criteria(PSD, Focus Area, Activity Type & 4s)
  getCriteriaValue(event) {
    this.criterionValue = "";
    this.criteriaValuesArr = [];
    this.criteriaVal_loader = true;
    this.actiPerform = {};
    this.studentActivityList = false;
    this.performedActiArr = [];
    this.showCriteriaValues = false; // to show the Criteria Values on when Criteria is selected
    this.teacherService.getAwardCriteriaValue().subscribe((res) => {
      this.criteriaValuesArr = res;
      // console.log(this.criteriaValuesArr);
      switch (event) {
        case "PSD Area": {
          this.criteriaValuesArr = res["PSD Areas"];
          this.criteriaVal_loader = false;
          break;
        }
        case "Focus Area": {
          this.criteriaValuesArr = res["Focus Areas"];
          this.criteriaVal_loader = false;
          break;
        }
        case "4S": {
          this.criteriaValuesArr = res["Four S"];
          this.criteriaVal_loader = false;
          break;
        }
        case "Activity Type": {
          this.criteriaValuesArr = this.activities;
          this.criteriaVal_loader = false;
          break;
        }
      }

    }, (err) => { console.log(err) })
  }

  // select all the Activities performed by a particular student
  getPerformedIds(activity) {
    this.actiPerform = {};
    this.studentId = activity.id;
    console.log(activity);
    activity.performedActivities.forEach((ele) => {
      ele.responses.forEach(element => {
        this.actiPerform[element.id] = true;
      });
    });
  }

  // set Start Date for Activity Performed Duration
  setStartDate() {
    const minDate = new Date();
    minDate.setMonth(minDate.getMonth() - 4);
    let month: any = minDate.getMonth() + 1;
    let day: any = minDate.getDate();
    let year: any = minDate.getFullYear();

    if (month < 10)
      month = '0' + month.toString();
    if (day < 10)
      day = '0' + day.toString();
    this.startDate = [year, month, day].join('-');
  }

  // set Till Date for Activty Performed Duration
  setEndDate() {
    const minDate = new Date();
    let month: any = minDate.getMonth() + 1;
    let day: any = minDate.getDate();
    let year: any = minDate.getFullYear();

    if (month < 10)
      month = '0' + month.toString();
    if (day < 10)
      day = '0' + day.toString();
    this.endDate = [year, month, day].join('-');
  }

  // to count the number of words 
  wordCount(e) {
    if (e) {
      this.count = e.split(/\s\w/).length;
    } else {
      this.count = 0;
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
