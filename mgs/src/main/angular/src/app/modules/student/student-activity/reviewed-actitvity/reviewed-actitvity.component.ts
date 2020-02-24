import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-reviewed-actitvity',
  templateUrl: './reviewed-actitvity.component.html',
  styleUrls: ['./reviewed-actitvity.component.scss']
})
export class ReviewedActitvityComponent implements OnInit {

  reviewedActivitiesArr = ["Sky Diving"];

  constructor(private studentService : StudentService) { }

  ngOnInit() {
  }

}
