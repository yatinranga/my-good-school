import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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
  reviewedActivitiesArr = [];
  allActivitiesArr = [];
  teacherInfo: any;
  teacherId: any;
  activityId = "";
  studentId = "";
  i: any;

  reviewForm: FormGroup;


  constructor(private teacherSerivce: TeacherService, private alertService: AlertService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.teacherId = this.teacherInfo['teacher'].id;
    this.teacherSerivce.getPendingActivity(this.teacherId).subscribe((res) => {
      this.activities = res;
      this.pendingActivitiesArr = this.activities.filter((e) => (e.activityStatus == "SubmittedByStudent"));
      this.savedActivitiesArr = this.activities.filter((e) => (e.activityStatus == "SavedByTeacher"));
    },
      (err) => console.log(err));
    this.allActivity();
    this.reviewFormInit();
    this.reviewedActivities();
  }

  allActivity(){
    this.teacherSerivce.getAllActivity(this.teacherId).subscribe((res) => {
      console.log(res);
      this.allActivitiesArr = res;
    },
    (err) => console.log(err));
  }

  reviewedActivities(){
    this.teacherSerivce.getReviewedActivity(this.teacherId).subscribe((res) => {
      console.log("reviewed");
      console.log(res);
      this.reviewedActivitiesArr = res;
    },
    (err) => console.log(err))
  }

  // Initialize Review Form
  reviewFormInit() {
    this.reviewForm = this.formBuilder.group({
      achievementScore: ['', [Validators.min(0), Validators.max(5)]],
      participationScore: ['', [Validators.min(0), Validators.max(10)]],
      initiativeScore: ['', [Validators.min(0), Validators.max(10)]],
      star: [''],
      coachRemark: ['']
    })
  }

  // Save/Review Pending Activity
  saveReview() {
    console.log(this.reviewForm.value);
    const formData = new FormData();
    formData.append('id', this.activityId);
    formData.append('coachId', this.teacherId);
    formData.append('studentId', this.studentId);
    formData.append('coachRemark', this.reviewForm.value.coachRemark);
    formData.append('participationScore', this.reviewForm.value.participationScore);
    formData.append('initiativeScore', this.reviewForm.value.initiativeScore);
    formData.append('achievementScore', this.reviewForm.value.achievementScore);
    formData.append('star', this.reviewForm.value.star);

    this.teacherSerivce.saveReviewedActivity(formData).subscribe((res) => {
      console.log(res);
      // this.savedActivitiesArr = [...this.savedActivitiesArr, ...this.pendingActivitiesArr.splice(this.i, 1)];
      this.reviewForm.reset();
      this.alertService.showSuccessToast('Review Saved !');
    },
      (err) => console.log(err)
    );
  }

  // Edit the Saved activity by teacher
  editSavedActivity(activity) {

    // e.stopPropagation();

    console.log(activity);
    this.activityId = activity.id;
    this.studentId = activity.studentId;

    this.reviewForm.patchValue({
      participationScore: activity.participationScore,
      achievementScore: activity.achievementScore,
      initiativeScore: activity.initiativeScore,
      star: activity.star,
      coachRemark: activity.coachRemark
    })
  }

  // SUBMIT the saved activity by teacher
  submitSavedActivity(e, index , status? :any ) {
    e.stopPropagation();
    var actCid : any;
    if(status === 'All'){
      actCid = this.allActivitiesArr[index].id;      
    } else {
      actCid = this.savedActivitiesArr[index].id;
    }
    
    console.log(actCid);
    this.teacherSerivce.submitActivity(actCid).subscribe((res) => {
      console.log(res);
      // this.savedActivitiesArr.splice(index,1); // to splice the selected activity and push into reviewedActivitesArr
      this.alertService.showSuccessToast('Activity Submitted !');
    },
      (err) => console.log(err));
  }

  reviewActivity(activity) {
    console.log(activity);
    this.activityId = activity.id
    this.studentId = activity.studentId;

    // console.log(index)
    console.log(this.activityId);
    console.log(this.studentId);
  }

  activityView(event) {
    this.activityType = event;
  }

  getDate(date) {
    return new Date(date)
  }

  resetForm() {
    this.reviewForm.reset();
  }

}
