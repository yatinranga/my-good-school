import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';


@Component({
  selector: 'app-teacher-awards',
  templateUrl: './teacher-awards.component.html',
  styleUrls: ['./teacher-awards.component.scss']
})
export class TeacherAwardsComponent implements OnInit {

  awardsList = [];
  schoolGrades = [];
  studentList = [];
  awardViewType = "assign";
  teacherInfo: any;
  schoolId = "";
  activities = [];
  loader : boolean = false ;

  gradeId = "";
  activityId = ""
  teacherId = "";

  createAwardForm: FormGroup;
  assignAwardForm: FormGroup;

  constructor(private teacherService: TeacherService, private formbuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.teacherId = this.teacherInfo['teacher'].id;

    this.assignAwardInit();

    this.getSchoolAwards();
    this.getSchoolGrades();
    this.getStudentActivity();

    this.createAwardForm = this.formbuilder.group({
      name: [''],
      description: [''],
      teacherId: ['']
    })
    this.assignAwardInit();
  }

  // initialize the Assign Award Form
  assignAwardInit() {
    this.assignAwardForm = this.formbuilder.group({
      id: [''],
      teacherId: [''],
      schoolId: [''],
      gradeId: [''],
      activityId: [''],
      studentId : [''],
      activityPerformedIds: ['']
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
    this.teacherService.getGrades(this.schoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
      (err) => console.log(err));
  }

  // get the Activities Offered in School
  getStudentActivity() {
    this.teacherService.getActivity(this.schoolId).subscribe((res) => {
      this.activities = res;
    },
      (err) => console.log(err)
    );
  }

  //  get LIST of students who performed specific activity of particular grade
  getListOfStudent() {
    this.teacherService.getStudents(this.schoolId, this.gradeId, this.activityId, this.teacherId).subscribe((res) => {
      console.log("Student List - ");
      console.log(res);
      this.studentList = res
    },
      (err) => console.log(err));
  }

  // Assign Award to Students
  assignAward() {
    console.log(this.assignAwardForm.value);

    this.teacherService.assignAward(this.assignAwardForm.value).subscribe((res) => {
      console.log(res);
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

  getActivityId(event) {
    console.log("getActivityId called ");
    this.activityId = event;
    this.getListOfStudent();
  }

  getStudentId(event) {
    console.log("StudentID called");
    console.log(event);
  }

  getGradeId(event) {
    this.gradeId = event;
  }

  awardView(type) {
    this.awardViewType = type;
  }

}
