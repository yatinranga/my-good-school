import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';
import { element } from 'protractor';

declare let $: any;

@Component({
  selector: 'app-teacher-awards',
  templateUrl: './teacher-awards.component.html',
  styleUrls: ['./teacher-awards.component.scss']
})
export class TeacherAwardsComponent implements OnInit {

  awardsList = [];
  schoolGrades = [];
  studentList = [];
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
  grade_loader: boolean;
  stu_loader: boolean;
  acti_loader: boolean;
  pa_loader: boolean; // Performed Activities Details Loader

  selectedGrade = "";
  selectedStudent = "";
  selectedActivity = "";

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

  constructor(private teacherService: TeacherService, private formbuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.teacherId = this.teacherInfo['teacher'].id;
    this.assignAwardInit();
    this.getSchoolGrades();
    this.awardView(this.awardViewType);
    this.getSchoolActivities();
  }

  // initialize the Assign Award Form
  assignAwardInit() {
    this.assignAwardForm = this.formbuilder.group({
      teacherId: [],
      schoolId: [],
      // studentId: [],
      activityId: [],
      awardType: [, [Validators.required]],
      description: [, [Validators.required, Validators.minLength(40)]],
      vaidFrom: [, [Validators.required]],
      validUntil: [, [Validators.required]],
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
    this.grade_loader = true;
    this.teacherService.getGrades(this.schoolId).subscribe((res) => {
      this.schoolGrades = res;
      this.grade_loader = false;
    },
      (err) => console.log(err));
  }

  //  get LIST of students who performed specific activity of particular grade
  // getListOfStudent(gradeId) {
  //   this.studentActivityList = false; // List of performed activities
  //   this.stu_loader = true;

  //   this.studentList = [];
  //   this.performedActiArr = [];
  //   this.activities = [];
  //   this.selectedStudent = "";
  //   this.selectedActivity = "";

  //   this.teacherService.getStudents(gradeId).subscribe((res) => {
  //     this.studentList = res;
  //     console.log(res);
  //     this.stu_loader = false;
  //   }, (err) => {
  //     console.log(err);
  //     this.stu_loader = false;
  //   });
  // }

  // get StudentId of selected from dropdown
  // getStudentId(studentId) {
  //   this.acti_loader = true;
  //   this.selectedActivity = "";
  //   this.studentId = studentId;
  //   this.activities = [];
  //   this.performedActiArr = [];
  //   this.studentActivityList = false;
  //   this.teacherService.getStudentActivities(studentId).subscribe((res) => {
  //     console.log(res);
  //     const activityIds = []
  //     this.activities = res.filter(a => {
  //       if (activityIds.includes(a.activityId))
  //         return false;
  //       else {
  //         activityIds.push(a.activityId);
  //         return true;
  //       }
  //     });
  //     this.acti_loader = false;

  //   },
  //     (err) => {
  //       console.log(err)
  //       this.acti_loader = false;
  //     });
  // }

  // get ActivityId of selected from dropdown
  // getActivityId(event) {
  //   this.activityId = event;
  //   // this.getPerformedActivities();
  //   this.studentActivityList = true;
  //   this.actiPerform = {};
  // }

  // Assign Award to Students
  assignAward() {
    console.log(this.actiPerform);
    this.pa_loader = true;
    this.assignAwardForm.value.teacherId = this.teacherId;
    this.assignAwardForm.value.schoolId = this.schoolId;
    // this.assignAwardForm.value.activityId = this.activityId;
    this.assignAwardForm.value.studentId = this.studentId;
    this.assignAwardForm.value.activityPerformedIds = [];
    Object.keys(this.actiPerform).forEach((key) => {
      if (this.actiPerform[key]) {
        this.assignAwardForm.value.activityPerformedIds.push(key);
      }
    })

    console.log(this.assignAwardForm.value);

    if (this.assignAwardForm.value.vaidFrom == this.assignAwardForm.value.validUntil) {
      this.alertService.showErrorAlert("Date cannot be same");
      this.pa_loader = false;
    } else if (this.assignAwardForm.value.vaidFrom > this.assignAwardForm.value.validUntil) {
      this.alertService.showErrorAlert("Valid Date cannot be ahead of Till Date");
      this.pa_loader = false;
    } else {
      this.assignAwardForm.value.vaidFrom = this.assignAwardForm.value.vaidFrom + " 00:00:00";
      this.assignAwardForm.value.validUntil = this.assignAwardForm.value.validUntil + " 00:00:00";
      this.teacherService.assignAward(this.assignAwardForm.value).subscribe((res) => {
        console.log(res);
        $('#assignAwardModal').modal('hide');
        $('.modal-backdrop').remove();
        this.performedActiArr = [];
        this.assignAwardForm.reset();
        this.alertService.showSuccessAlert("");
        this.studentActivityList = false;
        this.awardViewType = "view";
        this.pa_loader = false;
        this.viewAwards();
      },
        (err) => {
          console.log(err);
          // $('#assignAwardModal').modal('hide');
          // $('.modal-backdrop').remove();
          this.pa_loader = false;
        });
    }


  }

  // get List of Performed activity by specific activityId
  getPerformedActivities(value, type) {
    this.pa_loader = true;
    this.studentActivityList = true;
    // console.log(this.criterionValue)
    this.performedActiArr = [];
    if (type == "value") {
      if (this.gradeId == "") {
        this.criterionValue = value;
        this.teacherService.getStudentPerformedActivities(this.awardCriterion, value).subscribe((res) => {
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
        this.teacherService.getStudentPerformedActivities(this.awardCriterion, value, this.gradeId).subscribe((res) => {
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
        this.teacherService.getStudentPerformedActivities(this.awardCriterion, this.criterionValue, value).subscribe((res) => {
          console.log(res);
          this.performedActiArr = res;
        }, (err) => {
          console.log(err);
          this.pa_loader = false;
        });
      }
    }
  }

  // set award id in form 
  // setAwardId(event) {
  //   this.assignAwardForm.value.id = event;
  // }

  // get grades
  // getGradeId(event) {
  //   this.gradeId = event;
  // }

  // change award view
  awardView(type) {
    this.awardViewType = type;
    console.log("Award View Type = " + this.awardViewType);
    this.viewAwards();
    if (type == "assign") {
      this.awardCriteria();
    }
  }

  viewAwards() {
    if (this.awardViewType == "view") {
      this.selectedActivity = ""; // Reset the value of Activity dropdown
      this.selectedGrade = "";  // Reset the value of Grade dropdown
      this.selectedStudent = ""; // Reset the value of Student dropdown

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
    this.award_loader = true;
    this.teacherService.getAwards().subscribe((res) => {
      this.schoolAwards = res;
      this.award_loader = false;
    },
      (err) => {
        console.log(err);
        this.award_loader = false;
      });
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

  // to DOWNLOAD the Attachments
  downloadFile(url) {
    this.teacherService.downloadAttachment(url).subscribe((res) => {
      console.log(res);
    }, (err) => { console.log(err) });
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

  getPerformedIds(activity) {
    this.studentId = activity.id;
    activity.performedActivities.forEach((ele) => {
      this.actiPerform[ele.id] = true;
    });
  }


}
