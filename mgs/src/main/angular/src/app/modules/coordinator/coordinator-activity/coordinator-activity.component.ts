import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { BASE_URL } from 'src/app/services/app.constant';

@Component({
  selector: 'app-coordinator-activity',
  templateUrl: './coordinator-activity.component.html',
  styleUrls: ['./coordinator-activity.component.scss']
})
export class CoordinatorActivityComponent implements OnInit {
  BASE_URL: string;

  performedActiArr = [];
  copyPerformedActiArr = [];
  acti_loader: boolean = false;
  activityType = "All";

  constructor(private teacherService: TeacherService) {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
  }

  ngOnInit() {
    this.getPerformedActivities();
  }

  getPerformedActivities() {
    this.acti_loader = true;
    this.teacherService.getSupervisedActivities().subscribe(res => {
      this.performedActiArr = res;
      this.copyPerformedActiArr = Object.assign([], res);
      this.acti_loader = false;
    }, (err => {
      console.log(err);
      this.acti_loader = false;

    }))
  }
  // Toggle Activity View
  filterActivity() {
    this.performedActiArr = this.filter(Object.assign([], this.copyPerformedActiArr));
  }

  // Actual Filtering on the basis of Activity Type
  filter(array: any[]) {
    let filterActivitiesArr = [];
    if (this.activityType == "Reviewed") {
      filterActivitiesArr = array.filter(e => e.activityStatus && e.activityStatus == "Reviewed");
    }
    else if (this.activityType == "Pending") {
      filterActivitiesArr = array.filter(e => e.activityStatus && (e.activityStatus == "SavedByTeacher" || e.activityStatus == "SubmittedByStudent"));
    }
    else {
      filterActivitiesArr = array;
    }
    return filterActivitiesArr;
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
