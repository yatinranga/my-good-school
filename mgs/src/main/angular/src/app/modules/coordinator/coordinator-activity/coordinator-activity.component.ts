import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';

@Component({
  selector: 'app-coordinator-activity',
  templateUrl: './coordinator-activity.component.html',
  styleUrls: ['./coordinator-activity.component.scss']
})
export class CoordinatorActivityComponent implements OnInit {

  performedActiArr = [];
  acti_loader: boolean = false;
  constructor(private teacherService: TeacherService) { }

  ngOnInit() {
    this.getPerformedActivities();
  }

  getPerformedActivities() {
    this.acti_loader = true;
    this.teacherService.getSupervisedActivities().subscribe(res => {
      console.log(res);
      this.performedActiArr = res;
      this.acti_loader = false;
    }, (err => {
      console.log(err);
      this.acti_loader = false;

    }))
  }

  order: boolean = false;
  sortByStatus() {
    this.order = !this.order;
    // sort by activityStatus
    this.performedActiArr.sort((a, b) => {
      const nameA = a.activityStatus.toUpperCase(); // ignore upper and lowercase
      const nameB = b.activityStatus.toUpperCase(); // ignore upper and lowercase
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

}
