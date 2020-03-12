import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';
import { Router } from '@angular/router';
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

  awardViewType = "view";
  teacherInfo: any;
  schoolId = "";
  activities = [];
  studentActivityList = false;
  actiPerform = {};
  studentId = "";

  gradeId = "";
  activityId = ""
  teacherId = "";


  award_loader: boolean;
  grade_loader: boolean;
  stu_loader: boolean;
  acti_loader: boolean;
  pa_loader: boolean; // Performed Activities Details Loader


  createAwardForm: FormGroup;
  assignAwardForm: FormGroup;

  constructor(private teacherService: TeacherService, private formbuilder: FormBuilder, private alertService: AlertService,
    private router: Router) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.teacherId = this.teacherInfo['teacher'].id;
    this.awardView('view');
    this.assignAwardInit();
    this.getSchoolGrades();
    this.awardView(this.awardViewType);

    this.createAwardForm = this.formbuilder.group({
      name: [''],
      description: [''],
      teacherId: ['']
    })

  }

  // initialize the Assign Award Form
  assignAwardInit() {
    this.assignAwardForm = this.formbuilder.group({
      teacherId: [],
      schoolId: [],
      studentId: [],
      activityId: [],
      name: [, [Validators.required]],
      description: [, [Validators.required, Validators.maxLength(255)]],
      activityPerformedIds: [([])],
    });
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
  getListOfStudent(gradeId) {
    this.stu_loader = true;
    this.teacherService.getStudents(gradeId).subscribe((res) => {
      this.studentList = res;
      console.log(res);
      this.stu_loader = false;
    },
      (err) => console.log(err));
  }

  // Assign Award to Students
  assignAward() {
    this.assignAwardForm.value.teacherId = this.teacherId;
    this.assignAwardForm.value.schoolId = this.schoolId;
    this.assignAwardForm.value.activityId = this.activityId;
    this.assignAwardForm.value.studentId = this.studentId;
    this.assignAwardForm.value.activityPerformedIds = [];
    Object.keys(this.actiPerform).forEach((key) => {
      if (this.actiPerform[key]) {
        this.assignAwardForm.value.activityPerformedIds.push(key);
      }
    })

    console.log(this.assignAwardForm.value);

    this.teacherService.assignAward(this.assignAwardForm.value).subscribe((res) => {
      console.log(res);
      $('#assignAwardModal').modal('hide');
      $('.modal-backdrop').remove();
      this.performedActiArr = [];
      this.assignAwardForm.reset();
      this.alertService.showSuccessAlert("");
      this.studentActivityList = false;
      this.awardViewType = "view";
      this.viewAwards();
    },
      (err) => {
        console.log(err);
        $('#assignAwardModal').modal('hide');
        $('.modal-backdrop').remove();
      });

  }

  // get ActivityId of selected from dropdown
  getActivityId(event) {
    this.activityId = event;
    this.getPerformedActivities();
    this.studentActivityList = true;
  }

  // get List of Performed activity by specific activityId
  getPerformedActivities() {
    this.pa_loader = true;
    this.performedActiArr = [];
    this.teacherService.getStudentPerformedActivities(this.studentId, this.activityId).subscribe((res) => {
      console.log(res);
      this.performedActiArr = res;
      this.pa_loader = false;
    },
      (err) => console.log(err));
  }

  // get StudentId of selected from dropdown
  getStudentId(studentId) {
    this.acti_loader = true;
    this.studentId = studentId;
    this.activities = [];
    this.teacherService.getStudentActivities(studentId).subscribe((res) => {
      console.log(res);
      const activityIds = []
      this.activities = res.filter(a => {
        if (activityIds.includes(a.activityId))
          return false;
        else {
          activityIds.push(a.activityId);
          return true;
        }
      });
      this.acti_loader = false;

    },
      (err) => {
        console.log(err)
        this.acti_loader = false;
      });


    // to get performed activites of student
    // this.teacherService.getStudentPerformedActivities(event, this.activityId).subscribe((res) => {
    //   console.log(res);
    //   this.performedActiArr = res;
    // },
    //   (err) => {
    //     console.log(err)
    //   });

    // this.studentActivityList = true;
  }

  // set award id in form 
  setAwardId(event) {
    this.assignAwardForm.value.id = event;
  }

  // get grades
  getGradeId(event) {
    this.gradeId = event;
  }

  // change award view
  awardView(type) {
    this.awardViewType = type;
    console.log("Award View Type = " + this.awardViewType);
    this.viewAwards();
  }

  viewAwards() {
    if (this.awardViewType == "view") {
      this.award_loader = true;
      this.teacherService.getTeacherAwards().subscribe((res) => {
        console.log(res);
        this.awardsList = res;
        this.award_loader = false;
      },
        (err) => {
          console.log(err)
          this.award_loader = false;
        });
    }
  }

  // Verify the Selected Award 
  verifySelectedAward(e, i) {
    e.stopPropagation();
    const submit = confirm("Are you sure ?");
    if (submit) {
      const awardId = this.awardsList[i].id;
      console.log(awardId);

      this.teacherService.verifyAwards(awardId).subscribe((res) => {
        console.log(res);
        this.alertService.showSuccessAlert("");
        this.awardsList[i].status = "VERIFIED";
      },
        (err) => {
          console.log(err);

        })
    }


  }


  // stop toogle of table
  stopCollapse(e) {
    e.stopPropagation();
  }

}
