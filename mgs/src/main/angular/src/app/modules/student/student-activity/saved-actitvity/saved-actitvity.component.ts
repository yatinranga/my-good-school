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

  studentInfo = {};
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
  activityType = 'All';

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    // tslint:disable-next-line:no-string-literal
    this.studentId = this.studentInfo['student'].id;
    // tslint:disable-next-line:no-string-literal
    this.schoolId = this.studentInfo['student'].schoolId;

    // this.getStudentSavedActivities(this.studentInfo['student'].id)
    // this.getStudentSavedActivities(this.studentId);
    // this.getStudentSubmittedActivities(this.studentId);
    this.getStudentAllActivities(this.studentId);

    // this.getStudentActivity(this.schoolId);

    this.savedActivityForm = this.formBuilder.group({
      savedActivityName: [''],
      activityId: [''],
      description: [''],
      dateOfActivity: [''],
      coachId: [''],
      attachment: [''],
      id: ['']
    });
  }

  // to get the list of SAVED Activities of student
  getStudentSavedActivities(studentId) {
    this.studentService.getSavedActivity(studentId).subscribe((res) => {
      this.savedActivitiesArr = res;
    },
      (err) => console.log(err));
  }

  // to get the list of SUBMITTED Activities of student
  getStudentSubmittedActivities(studentId) {
    this.studentService.getSubmittedActivity(studentId).subscribe((res) => {
      this.submittedActivitiesArr = res;
    },
      (err) => console.log(err)
    );
  }

  // to get the list of ALL Activities of student
  getStudentAllActivities(studentId) {
    this.studentService.getAllActivity(studentId).subscribe((res) => {
      this.allActivitiesArr = res;
      this.allActivitiesArr = res.filter((e) => (e.activityStatus != "SavedByTeacher"));
      this.savedActivitiesArr = this.allActivitiesArr.filter((e) => (e.activityStatus == "SavedByStudent"));
      this.submittedActivitiesArr = this.allActivitiesArr.filter((e) => (e.activityStatus == "SubmittedByStudent"));
      this.reviewedActivitiesArr = this.allActivitiesArr.filter((e) => (e.activityStatus == "Reviewed"));
      console.log(this.allActivitiesArr);
    },
      (err) => console.log(err)
    );
  }

  // on click of edit button
  editSavedActivity(e,activity) {
    // this.activityName = activity.activityName;
    // this.activityTeacher = activity.teacherName;
    e.stopPropagation();
    console.log(activity);
    this.activityId = activity.activityId;

    const date = this.getDate(activity.dateOfActivity);
    if ((date.getMonth() + 1) < 10) {
      if (date.getDate() < 10) {
        this.savedActivityForm.controls.dateOfActivity.patchValue(date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)) + '-' + ('0' + date.getDate()));
      } else {
        this.savedActivityForm.controls.dateOfActivity.patchValue(date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)) + '-' + date.getDate());
      }
    } else {
      if (date.getDate() < 10) {
        this.savedActivityForm.controls.dateOfActivity.patchValue(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + ('0' + date.getDate()));
      } else {
        this.savedActivityForm.controls.dateOfActivity.patchValue(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate());
      }
    }

    this.savedActivityForm.patchValue({
      activityId: activity.activityId,
      description: activity.description,
      id: activity.id,
    });

    console.log(this.savedActivityForm.value);


    this.getStudentActivity(this.schoolId);
    return this.editActivityShow = true;
  }

  // to SUBMIT the activity
  submitSavedActivity(e, index) {
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

  deleteSavedActivity(e,activity,i) {
    e.stopPropagation();
    const activityId = activity.id;
    console.log(activityId);
    this.studentService.deleteActivity(activityId).subscribe((res) => {
      console.log(res);
      this.savedActivitiesArr.splice(i,1);
      this.alertService.showSuccessToast('Activity Deleted !');
    },
      (err) => console.log(err));
  }

  onCancel() {
    return this.editActivityShow = false;
  }

  // to get all activities of particular school
  getStudentActivity(schoolId) {
    this.studentService.getActivity(schoolId).subscribe((res) => {
      this.activities = res;
      this.getStudentCoach(this.activityId);
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
      },
      (err) => console.log(err)
    );
  }

  activityView(event) {
    this.activityType = event;
  }

  getDate(date) {
    return new Date(date);
  }

}
