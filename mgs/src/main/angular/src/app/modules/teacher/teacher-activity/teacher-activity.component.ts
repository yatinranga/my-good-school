import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-teacher-activity',
  templateUrl: './teacher-activity.component.html',
  styleUrls: ['./teacher-activity.component.scss']
})
export class TeacherActivityComponent implements OnInit {
  activityType = "Pending";
  activities = []
  pendingActivitiesArr = [];
  savedActivitiesArr = [];
  submittedActivitesArr = [];
  teacherInfo: any;
  teacherId: any;

  reviewForm : FormGroup;

  constructor(private teacherSerivce: TeacherService, private alertService : AlertService, private formBuilder : FormBuilder) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.teacherId = this.teacherInfo['teacher'].id;
    this.teacherSerivce.getPendingActivity(this.teacherId).subscribe((res) => {
      this.activities = res;
      console.log(this.activities);
      this.pendingActivitiesArr = this.activities.filter((e) => (e.activityStatus == "SubmittedByStudent"));
      this.savedActivitiesArr = this.activities.filter((e) => (e.activityStatus == "SavedByTeacher" ));
    },
    (err) => console.log(err));
    this.reviewFormInit();
  };

  reviewFormInit(){
    this.reviewForm = this.formBuilder.group({
      participationScore : [''],
      AchievementScore : [''],
      InitiativeScore : [''],
      Star : [''],
      TeacherRemark : ['']
    })
  }

  saveReview(){
    console.log(this.reviewForm.value);
  }

  // SUBMIT the saved activity by teacher
  submitSavedActivity(index){
    const actCid = this.savedActivitiesArr[index].id;
    this.teacherSerivce.submitActivity(actCid).subscribe((res) => {
      console.log(res);
      // this.savedActivitiesArr.splice(index,1); // to splice the selected activity and push into submittedActivitesArr
      this.alertService.showSuccessToast('Activity Submitted !');
    },
    (err) => console.log(err));
  }

  reviewActivity(activity){
    console.log(activity.id);
  }

  activityView(event) {
    this.activityType = event;
  }
  
  getDate(date) {
    return new Date(date)
  }

}
