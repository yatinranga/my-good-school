import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-saved-actitvity',
  templateUrl: './saved-actitvity.component.html',
  styleUrls: ['./saved-actitvity.component.scss']
})
export class SavedActitvityComponent implements OnInit {

  addActivityShow: boolean;
  studentInfo: any = [];
  savedActivitiesArr = [];
  submittedActivitiesArr = [];
  reviewedActivitiesArr = [];
  allActivitiesArr = [];
  activities = [];
  coaches = [];
  schoolId: any;
  studentId: any;
  editActivityShow = false;
  savedActivityForm: FormGroup;

  activityName: any;
  activityDetails: any;
  activityTeacher: any;
  activityId: any;
  activityDate: any;
  file = [];
  url = '';
  activityType = 'Submitted';
  loader: boolean = false;

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.schoolId = this.studentInfo['student'].schoolId;
    this.activityView(this.activityType);
    this.getStudentActivity(this.schoolId);

    this.savedActivityForm = this.formBuilder.group({
      activityId: [''],
      description: [''],
      dateOfActivity: [''],
      coachId: [''],
      attachment: [],
      id: ['']
    });
  }

  // to get the list of SAVED Activities of student
  getStudentSavedActivities(studentId) {
    this.studentService.getSavedActivity(studentId).subscribe((res) => {
      this.savedActivitiesArr = res;
      console.log(res);
      this.loader = false;
    }, (err) => {
      this.loader = false;
      console.log(err)
    })
  }

  // to get the list of SUBMITTED Activities of student
  getStudentSubmittedActivities(studentId) {
    this.studentService.getSubmittedActivity(studentId).subscribe((res) => {
      this.submittedActivitiesArr = res;
      this.loader = false;
    }, (err) => {
      this.loader = false;
      console.log(err)
    });
  }

  // to get the list of ALL Activities of student
  getStudentAllActivities(studentId) {
    this.studentService.getAllActivity(studentId).subscribe((res) => {
      console.log(res);
      this.allActivitiesArr = res;
      this.allActivitiesArr = this.allActivitiesArr.filter((e) => (e.activityStatus != "SavedByTeacher"));
      this.loader = false;
    }, (err) => {
      this.loader = false;
      console.log(err)
    }
    );
  }

  // to get the list of REVIEWED Activities of student
  getStudentReviewedActivities(studentId) {
    this.studentService.getReviewedActivity(studentId).subscribe((res) => {
      this.reviewedActivitiesArr = res;
      this.loader = false;
    }, (err) => {
      this.loader = false;
      console.log(err)
    })

  }

  // on click of edit button
  editSavedActivity(e, activity) {
    e.stopPropagation();
    console.log(activity);
    this.activityId = activity.activityId;
    this.getStudentCoach(this.activityId);
    this.savedActivityForm.controls.dateOfActivity.patchValue(activity.dateOfActivity.split(' ')[0]);
    this.savedActivityForm.patchValue({
      activityId: activity.activityId,
      description: activity.description,
      coachId: activity.coachId,
      id: activity.id,
    });
    this.editActivityShow = true;
    this.addActivityShow = false;
  }

  addActivity() {
    this.savedActivityForm.reset();
    this.savedActivityForm.value.attachment = [];
    this.addActivityShow = true;
    this.editActivityShow = false;
  }

  // to SUBMIT the activity
  submitSavedActivity(e, index) {
    const submit = confirm("Do you want to submit ?");
    if (submit) {
      e.stopPropagation();
      const activityId = this.savedActivitiesArr[index].id;

      console.log(this.submittedActivitiesArr);
      this.studentService.submitActivity(activityId).subscribe((res) => {
        console.log(res);
        this.submittedActivitiesArr = [...this.submittedActivitiesArr, ...this.savedActivitiesArr.splice(index, 1)];
        this.alertService.showSuccessToast('Activity Submitted !');
      },
        (err) => console.log(err)
      );
    }
  }

  deleteSavedActivity(e, activity, i) {
    const submit = confirm("Do you want to delete ?");
    if (submit) {
      e.stopPropagation();
      const activityId = activity.id;
      console.log(activityId);
      this.studentService.deleteActivity(activityId).subscribe((res) => {
        console.log(res);
        this.savedActivitiesArr.splice(i, 1);
        this.alertService.showSuccessToast('Activity Deleted !');
      },
        (err) => console.log(err));
    }
  }

  onCancel() {
    this.editActivityShow = false;
    this.addActivityShow = false;
  }

  // to get all activities of particular school
  getStudentActivity(schoolId) {
    this.studentService.getActivity(schoolId).subscribe((res) => {
      this.activities = res;
    },
      (err) => console.log(err)
    );
  }

  // to get teacher/coach who perform selected activity
  getStudentCoach(activityId) {
    this.studentService.getCoach(this.schoolId, activityId).subscribe((res) => {
      this.coaches = res;
    },
      (err) => console.log(err)
    );
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.file = [...event.target.files];
      this.savedActivityForm.value.attachment = this.file;
      console.log(this.file);
    }
  }

  // to UPDATE the saved activity
  updateActivity() {
    if (this.editActivityShow) {
      const formData = new FormData();
      const date = new Date(this.savedActivityForm.value.dateOfActivity);
      const activityDate = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate();

      formData.append('studentId', this.studentInfo['student'].id);
      formData.append('activityId', this.savedActivityForm.value.activityId);
      formData.append('coachId', this.savedActivityForm.value.coachId);
      formData.append('dateOfActivity', activityDate);
      formData.append('description', this.savedActivityForm.value.description);
      formData.append('id', this.savedActivityForm.value.id);

      if (this.savedActivityForm.value.attachment.length > 0) {
        this.savedActivityForm.value.attachment.forEach((element, index) => {
          formData.append('fileRequests[' + index + '].file', element);
        });
      }

      this.studentService.addActivity('/api/student/activities', formData).subscribe(
        (res) => {
          console.log(res);
          this.alertService.showSuccessToast('Activity Updated !');
          this.editActivityShow = false;
        },
        (err) => console.log(err)
      );
    }

    if (this.addActivityShow) {
      const time = this.savedActivityForm.value.dateOfActivity + " 00:00:00";
      this.savedActivityForm.value.dateOfActivity = time;
      const formData = new FormData();
      formData.append('studentId', this.studentInfo.student.id);
      formData.append('activityId', this.savedActivityForm.value.activityId);
      formData.append('coachId', this.savedActivityForm.value.coachId);
      formData.append('dateOfActivity', time);
      formData.append('description', this.savedActivityForm.value.description);

      if (this.savedActivityForm.value.attachment.length > 0) {
        this.savedActivityForm.value.attachment.forEach((element, index) => {
          formData.append('fileRequests[' + index + '].file', element);
        });
      }

      console.log(this.savedActivityForm.value);

      this.studentService.addActivity("/api/student/activities", formData).subscribe(
        (res) => {
          console.log(res);
          this.savedActivitiesArr.push(res);
          this.allActivitiesArr.push(res);
          this.alertService.showSuccessToast('Activity Saved !');
          this.addActivityShow = false;
        },
        (err) => console.log(err)
      );
    }
  }

  activityView(event) {
    this.activityType = event;
    this.loader = true;
    switch (this.activityType) {
      case "All": this.getStudentAllActivities(this.studentId);
        break;

      case "Saved": this.getStudentSavedActivities(this.studentId);
        break;

      case "Reviewed": this.getStudentReviewedActivities(this.studentId);
        break;

      case "Submitted": this.getStudentSubmittedActivities(this.studentId);
        break;
    }
  }

  getDate(date) {
    return new Date(date);
  }

}