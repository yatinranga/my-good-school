import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';
declare let $:any;

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
  awardViewType = "assign";
  teacherInfo: any;
  schoolId = "";
  activities = [];
  studentActivityList = false;
  actiPerform = {};
  studentId = "";

  gradeId = "";
  activityId = ""
  teacherId = "";

  grade_loader: boolean;
  stu_loader : boolean;
  acti_loader : boolean;
  pa_loader : boolean; // Performed Activities Details Loader


  createAwardForm: FormGroup;
  assignAwardForm: FormGroup;

  constructor(private teacherService: TeacherService, private formbuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.teacherId = this.teacherInfo['teacher'].id;
    
    this.assignAwardInit();

    // this.getSchoolAwards();
    this.getSchoolGrades();
    // this.getSchoolActivity();
 

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
      schoolId :[],
      studentId :[],
      activityId : [],
      name : [,[Validators.required]],
      description : [,[Validators.required,Validators.maxLength(255)]],
      activityPerformedIds: [([])],
    });
  }

  // get AWARDS of School
  getSchoolAwards() {
    this.teacherService.getAwards(this.schoolId).subscribe((res) => {
      this.awardsList = res;
    },
      (err) => console.log(err));
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

  // get the Activities Offered in School
  getSchoolActivity() {
    this.teacherService.getActivity(this.schoolId).subscribe((res) => {
      this.activities = res;
    },
      (err) => console.log(err)
    );
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
      if(this.actiPerform[key]) {
        this.assignAwardForm.value.activityPerformedIds.push(key);
      }
    })

    console.log(this.assignAwardForm.value);

    this.teacherService.assignAward(this.assignAwardForm.value).subscribe((res) => {
      console.log(res);
      $('#assignAwardModal').modal('hide');
      this.performedActiArr = [];
      this.assignAwardForm.reset();
      this.alertService.showSuccessAlert("");      
    },
      (err) => console.log(err));

  }

  // create NEW Award
  createNewAward() {
    this.createAwardForm.value.teacherId = this.teacherId;
    this.teacherService.addAward(this.createAwardForm.value).subscribe((res) => {
      console.log(res);
      this.alertService.showSuccessToast('Award Created !');
      this.createAwardForm.reset();
    },
      (err) => console.log(err));
  }

  // get ActivityId of selected from dropdown
  getActivityId(event) {
    this.activityId = event;
    this.getPerformedActivities();
    this.studentActivityList = true;      
  }

  // get List of Performed activity by specific activityId
  getPerformedActivities(){
    this.pa_loader = true;
    this.performedActiArr = [];
    this.teacherService.getStudentPerformedActivities(this.studentId,this.activityId).subscribe((res) => {
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
      this.activities = res.filter( a => {
        if(activityIds.includes(a.activityId))
          return false;
        else{
          activityIds.push(a.activityId);
          return true;
        }
      });
      console.log(this.activities);
      this.acti_loader=false;     
      
    },
    (err) => console.log(err));


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
  setAwardId(event){
    this.assignAwardForm.value.id = event;
  }

  // get grades
  getGradeId(event) {
    this.gradeId = event;
  }

  // change award view
  awardView(type) {
    this.awardViewType = type;
    console.log("Type = "+type);
    console.log("Award View Type = "+this.awardViewType);
  }

  // stop toogle of table
  stopCollapse(e){
    e.stopPropagation();
  }

}
