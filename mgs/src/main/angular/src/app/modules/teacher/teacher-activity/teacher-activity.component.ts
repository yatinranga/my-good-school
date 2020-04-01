import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
declare let $: any;

@Component({
  selector: 'app-teacher-activity',
  templateUrl: './teacher-activity.component.html',
  styleUrls: ['./teacher-activity.component.scss']
})
export class TeacherActivityComponent implements OnInit {
  activityType = "All";

  pendingActivitiesArr = [];
  savedActivitiesArr = [];
  reviewedActivitiesArr = [];
  allActivitiesArr = [];
  teacherInfo: any;
  teacherId: any;
  activityId = "";
  studentId = "";
  index: any;
  loader: boolean = false;
  save_loader: boolean = true;

  reviewForm: FormGroup;
  selectedActivity: any;

  partiScore = 0; // Calc Participation Score
  initScore = 0; // Calc Initiative Score
  achiScore = 0; // Calc Achievement Score
  totalScore = 0; // Calc Total Marks

  activitiesArr = []; //single arr for performed actvities


  constructor(private teacherService: TeacherService, private alertService: AlertService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.teacherId = this.teacherInfo['teacher'].id;
    // this.teacherService.getPendingActivity(this.teacherId).subscribe((res) => {
    //   this.activities = res;
    //   this.pendingActivitiesArr = this.activities.filter((e) => (e.activityStatus == "SubmittedByStudent"));
    //   this.savedActivitiesArr = this.activities.filter((e) => (e.activityStatus == "SavedByTeacher"));
    // },
    //   (err) => console.log(err));

    this.reviewFormInit();
    this.activityView(this.activityType);

  }

  // Toggle Activity View
  activityView(event) {
    this.loader = true;
    this.activityType = event;
    switch (this.activityType) {
      case "All": this.allActivities(); break;
      case "Pending": this.pendingActivities(); break;
      case "Saved": this.savedActivities(); break;
      case "Reviewed": this.reviewedActivities(); break;
    }
  }

  // Initialize Review Form
  reviewFormInit() {
    this.reviewForm = this.formBuilder.group({
      achievementScore: [, [Validators.min(0), Validators.max(5)]],
      participationScore: [, [Validators.min(0), Validators.max(10)]],
      initiativeScore: [, [Validators.min(0), Validators.max(10)]],
      // star: [],
      coachRemark: []
    })
  }

  // PENDING Activities of Teacher
  pendingActivities() {
    this.activitiesArr = [];
    this.teacherService.getPendingActivity(this.teacherId).subscribe((res) => {
      // this.activities = res;
      // this.pendingActivitiesArr = this.activities.filter((e) => (e.activityStatus == "SubmittedByStudent"));
      this.pendingActivitiesArr = res.filter((e) => (e.activityStatus == "SubmittedByStudent"));
      this.activitiesArr = this.pendingActivitiesArr;
      console.log(this.pendingActivitiesArr);
      this.loader = false;
    },
      (err) => {
        console.log(err)
        this.loader = false;
      });
  }

  // Saved Activities of Teacher
  savedActivities() {
    this.activitiesArr = [];
    this.teacherService.getPendingActivity(this.teacherId).subscribe((res) => {
      this.savedActivitiesArr = res.filter((e) => (e.activityStatus == "SavedByTeacher"));
      this.activitiesArr = this.savedActivitiesArr;
      this.loader = false;
    },
      (err) => {
        console.log(err)
        this.loader = false;
      });
  }

  // All activities of Teacher
  allActivities() {
    this.activitiesArr = [];
    this.teacherService.getAllActivity(this.teacherId).subscribe((res) => {
      console.log(res);
      this.allActivitiesArr = res.filter((e) => (e.activityStatus != "SavedByStudent"));
      this.activitiesArr = this.allActivitiesArr;
      this.loader = false;
    },
      (err) => {
        console.log(err)
        this.loader = false;
      });
  }

  // REVIEWED Activities of Teacher
  reviewedActivities() {
    this.activitiesArr = [];
    this.teacherService.getReviewedActivity(this.teacherId).subscribe((res) => {
      console.log(res);
      this.reviewedActivitiesArr = res;
      this.activitiesArr = this.reviewedActivitiesArr;
      this.loader = false;
    },
      (err) => {
        console.log(err)
        this.loader = false;
      });
  }

  // Save/Review Pending Activity
  saveReview() {
    console.log(this.reviewForm.value);
    this.save_loader = true;
    const formData = new FormData();
    formData.append('id', this.activityId);
    formData.append('coachId', this.teacherId);
    formData.append('studentId', this.studentId);
    formData.append('coachRemark', this.reviewForm.value.coachRemark);
    formData.append('participationScore', this.reviewForm.value.participationScore);
    formData.append('initiativeScore', this.reviewForm.value.initiativeScore);
    formData.append('achievementScore', this.reviewForm.value.achievementScore);

    this.teacherService.saveReviewedActivity(formData).subscribe((res) => {
      console.log(res);
      if(this.activityType == "All" || this.activityType == "Saved"){
        this.activitiesArr.splice(this.index,1);
        this.activitiesArr.unshift(res);
      } else {
        this.activitiesArr.splice(this.index,1);
      }

      $('#reviewModal').modal('hide');
      $('.modal-backdrop').remove();
      this.reviewForm.reset();
      this.alertService.showSuccessToast('Review Saved !').then((response) => {
        this.save_loader = false;
        this.directSubmitReview(res.id);
      })
    },
      (err) => {
        console.log(err);
        this.save_loader = false;
      }
    );
  }

  // Edit the Saved activity by teacher
  editSavedActivity(activity,index, e) {
    this.index = index;
    e.stopPropagation();
    this.activityId = activity.id;
    this.studentId = activity.studentId;

    this.reviewForm.patchValue({
      participationScore: activity.participationScore,
      achievementScore: activity.achievementScore,
      initiativeScore: activity.initiativeScore,
      coachRemark: activity.coachRemark
    })
    $('#reviewModal').modal({
      backdrop: 'static',
      keyboard: false
    });
    $('#reviewModal').modal('show');
  }

  // SUBMIT the saved activity by teacher
  submitSavedActivity(activity,index, e) {
    e.stopPropagation();
    this.alertService.confirmWithoutLoader('question',"Do you want to submit ?",'','Yes').then(result => {
      if(result.value){
        var actCid: any;
        actCid = activity.id;
    
        console.log(actCid);
        this.teacherService.submitActivity(actCid).subscribe((res) => {
          console.log(res);
          if (this.activityType == "All") {
            this.activitiesArr.splice(index, 1);
            this.activitiesArr.unshift(res);
          } else {
            this.activitiesArr.splice(index, 1);
          }
          this.alertService.showSuccessToast('Activity Submitted !');
        },
          (err) => {console.log(err)});
      }
    })
  }

  directSubmitReview(activityId){
    this.alertService.confirmWithoutLoader('question',"Do you want to submit ?",'','Yes').then(result => {
      console.log(result);
      if(result.value)
        this.teacherService.submitActivity(activityId).subscribe((res) => {
          console.log(res);
          this.save_loader = false;
          if (this.activityType == "All") {
            this.activitiesArr.shift();
            this.activitiesArr.unshift(res);
          } else if (this.activityType == "Saved") {
            this.activitiesArr.shift();
          }
          this.alertService.showSuccessAlert('Review Submitted !');
        }, (err) => {
          console.log(err);
          this.save_loader = false;
        })
    })

  }

  reviewActivity(activity,index, e) {
    this.index = index;
    this.selectedActivity = activity;
    e.stopPropagation();
    this.activityId = activity.id
    this.studentId = activity.studentId;
    this.totalScore = activity.totalMarks;
    $('#reviewModal').modal({
      backdrop: 'static',
      keyboard: false
    });
    $('#reviewModal').modal('show');
  }

  getDate(date) {
    return new Date(date)
  }

  // to reset the Review Form
  resetForm() {
    this.reviewForm.reset();
  }

  order: boolean = false;
  sortByStatus() {
    this.order = !this.order;
    // sort by activityStatus
    this.allActivitiesArr.sort((a, b) => {
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

  // Calculate Total Marks
  calTotalMarks(scoreType,value){
    
    if (scoreType == "achievement"){
      this.achiScore = Number(value);
      console.log(typeof(value));     
    }
    if (scoreType == "participation"){
      this.partiScore = Number(value);            
    }
    if (scoreType == "initiative"){
      this.initScore = Number(value);            
    }

    if ((this.initScore > -1) && (this.partiScore > -1) && (this.achiScore > -1) 
        && (this.initScore < 11) && (this.partiScore < 11) && (this.achiScore < 6)) {
      this.totalScore = this.initScore + this.partiScore + this.achiScore;
      console.log("Total Score - " + this.totalScore);
    } else if ( this.totalScore > 25){
        this.totalScore  = 0;
    } else {
      this.totalScore = 0;
    }
  }

  // to DOWNLOAD the Attachments
  downloadFile(url){
    this.teacherService.downloadAttachment(url).subscribe((res) => {
      console.log(res);
    }, (err) => {console.log(err)});
  }


}

