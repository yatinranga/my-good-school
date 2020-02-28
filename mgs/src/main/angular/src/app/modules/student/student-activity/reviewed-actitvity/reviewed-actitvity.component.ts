import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-reviewed-actitvity',
  templateUrl: './reviewed-actitvity.component.html',
  styleUrls: ['./reviewed-actitvity.component.scss']
})
export class ReviewedActitvityComponent implements OnInit {

  reviewedActivitiesArr = ["Yoga"];
  studentId: any;
  studentInfo = [];
  // reviewedActivitiesArr = [];

  constructor(private studentService: StudentService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    // this.studentService.getReviewedActivity().subscribe((res) =>)

  }

}
