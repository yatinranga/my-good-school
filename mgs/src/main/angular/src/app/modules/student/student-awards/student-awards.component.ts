import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-student-awards',
  templateUrl: './student-awards.component.html',
  styleUrls: ['./student-awards.component.scss']
})
export class StudentAwardsComponent implements OnInit {
  studentInfo: any;
  schoolId: any;
  activities: any;
  studentId: any;
  awardsArr = [];
  loader : boolean = false ;

  constructor(private studentService : StudentService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.schoolId = this.studentInfo['student'].schoolId;
    this.getStudentActivity();
  }

  getStudentActivity() {
    this.studentService.getActivity(this.schoolId).subscribe(
      (res) => this.activities = res ,
      (err) => console.log(err)
    );
  }

  getStudentAwards(activityId){
    this.loader = true;
    console.log(activityId);

    if(activityId === "All"){
      this.studentService.getAllAwards(this.studentId).subscribe((res) => {
        this.awardsArr = res;
        console.log(res);
        this.loader = false;
      },
      (err) =>  console.log(err));
    }else{
      this.studentService.getAwards(this.studentId,activityId).subscribe((res) => {
        this.awardsArr = res;
        console.log(res);
        this.loader = false;
      },
      (err) => console.log(err));
    }
  }

  getDate(date) {
    return new Date(date)
  }
}
