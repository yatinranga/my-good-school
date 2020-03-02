import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';


@Component({
  selector: 'app-teacher-awards',
  templateUrl: './teacher-awards.component.html',
  styleUrls: ['./teacher-awards.component.scss']
})
export class TeacherAwardsComponent implements OnInit {

  awardsList = [];
  schoolGrades = [];
  studentList = ["Ram","John","Radhe"];
  awardViewType : any;
  teacherInfo: any;
  schoolId = "";
  activities: any;

  gradeId = "";
  activityId = ""
  teacherId = "";

  createAwardForm : FormGroup;

  constructor(private teacherService : TeacherService, private formbuilder : FormBuilder, private alertService : AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo['teacher'].schoolId;
    this.teacherId =  this.teacherInfo['teacher'].id;
    this.getSchoolAwards();
    this.getSchoolGrades();
    this.getStudentActivity();

    this.createAwardForm = this.formbuilder.group({
      name : [('')],
      description : [('')],
    })
  }

  // get AWARDS of School
  getSchoolAwards(){
    this.teacherService.getAwards(this.schoolId).subscribe((res) => {
      this.awardsList = res;
    },
    (err) => console.log(err));
  }

  // get Grades of School
  getSchoolGrades(){
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
  getListOfStudent(){
    this.teacherService.getStudents(this.schoolId,this.gradeId,this.activityId,this.teacherId).subscribe((res) => {
      this.studentList = res
    },
    (err) => console.log(err));
  }

  // create NEW Award
  createNewAward(){
    console.log(this.createAwardForm.value);
    const formData = new FormData();
    formData.append('name',this.createAwardForm.value.name);
    formData.append('description',this.createAwardForm.value.description);
    formData.append('teacherId',this.teacherId);

    this.teacherService.addAward(formData).subscribe((res) => {
      console.log(res);
      this.alertService.showSuccessToast('Award Created !');
    },
    (err) => console.log(err));
  }

  getActivityId(event){
    this.activityId = event;
    this.getListOfStudent();
  }

  getGradeId(event){
    this.gradeId = event;
  }

  awardView(type)
  {
    this.awardViewType = type;
  }

}
