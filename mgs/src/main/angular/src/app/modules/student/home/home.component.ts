import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  activities = [];
  coaches = [];
  students = [];
  grades = [];
  copyStuArr = [];

  studentInfo: any;
  studentId: any;
  schoolId: any;

  activityId = "";
  grade = "";

  stu_loader = false;
  sup_loader = false;

  constructor(private studentService: StudentService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.studentInfo.student.schoolId;
    this.getActivity(this.schoolId);
    this.getGrades(this.schoolId);
  }

  // get List of Activities of School
  getActivity(schoolId) {
    this.studentService.getActivity(schoolId).subscribe((res) => {
      this.activities = res;
    },
      (err) => console.log(err)
    );
  }

  // get List of Supervisior of Selected Activity
  getCoaches(actiId) {
    this.getStudents(actiId);
    this.coaches = [];
    this.students = [];
    this.grade = "";
    this.sup_loader = true;
    this.studentService.getCoach(this.schoolId, actiId).subscribe((res) => {
      this.coaches = res;
      this.sup_loader = false;
    }, (err) => {
      console.log(err);
      this.sup_loader = false;
    })
  }

  // get List of Student of selected Activity
  getStudents(actiId) {
    this.stu_loader = true;
    this.studentService.getActivityStudent(actiId).subscribe((res) => {      
      this.students = res;
      this.copyStuArr = Object.assign([], res);
      console.log(res);
      this.stu_loader = false;
    }, (err) => {
      console.log(err);
      this.stu_loader = false;
    });
  }

  getGrades(schoolId){
    this.studentService.getGradesOfSchool(schoolId).subscribe((res) => {
      this.grades = res;
    },(err) => {console.log(err)});
  }

  filterStudent(){
    this.students = this.filter(Object.assign([], this.copyStuArr));    
  }

  // Actual Filtering on the basis of PSD , Focus Area and 4S
  filter(array: any[]) {
    let filterStudentArr = [];
    if (this.grade) {
      filterStudentArr = array.filter(e => e.grade == this.grade);
    } else {
      filterStudentArr = array;
    }
    return filterStudentArr;
  }

}
