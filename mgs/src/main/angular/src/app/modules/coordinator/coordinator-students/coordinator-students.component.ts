import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';

@Component({
  selector: 'app-coordinator-students',
  templateUrl: './coordinator-students.component.html',
  styleUrls: ['./coordinator-students.component.scss']
})
export class CoordinatorStudentsComponent implements OnInit {

  coordinatorInfo: any;
  studentsArr = [];
  copyStuArr = [];
  schoolGrades = [];
  student_loader: boolean = false;
  search: any; // Used for search
  gradeId = "" // used to filter by grade
  col = "col-12";
  showDetails: boolean = false;
  student_obj: any;
  constructor(private teacherService: TeacherService) { }

  ngOnInit() {
    this.coordinatorInfo = JSON.parse(localStorage.getItem('user_info'))
    this.getStudents();
    this.getCoordinatorGrades();
  }

  getStudents() {
    this.student_loader = true;
    this.teacherService.getCoordinatorStudents(this.coordinatorInfo.id).subscribe(res => {
      this.studentsArr = res;
      this.copyStuArr = Object.assign([], res);
      this.student_loader = false;
    }, (err => {
      console.log(err);
      this.student_loader = false;
    }))
  }

  filterStudents() {
    this.studentsArr = this.filter(Object.assign([], this.copyStuArr));
  }

  filter(array: any[]) {
    let filterStuArr = []
    if (this.gradeId) {
      filterStuArr = array.filter(e => e.gradeId && e.gradeId == this.gradeId);
    }
    else {
      filterStuArr = array;
    }
    return filterStuArr;
  }

  getCoordinatorGrades() {
    this.teacherService.getProfile(this.coordinatorInfo.id).subscribe(res => {
      this.schoolGrades = res.grades;
    }, (err => {
      console.log(err);
    }))
  }

  /** Set Show Details */
  setShowDetails(val: boolean, student_obj?) {
    this.student_obj = student_obj;
    this.showDetails = val;
    this.showDetails ? (this.col = "col-7") : (this.col = "col-12");
  }

}
