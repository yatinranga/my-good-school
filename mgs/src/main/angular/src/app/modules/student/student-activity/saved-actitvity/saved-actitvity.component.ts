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

  studentInfo = [];
  savedActivitiesArr = [];
  activities = [];
  coaches = [];
  schoolId: any;
  studentId: any;
  editActivity = false;
  savedActivityForm: FormGroup;
  savedActivityId: any;

  submittedActivitiesArr = [];

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.schoolId = this.studentInfo['student'].schoolId;

    // this.getStudentSavedActivities(this.studentInfo['student'].id)
    this.getStudentSavedActivities(this.studentId);
    this.getStudentSubmittedActivities(this.studentId);

    this.savedActivityForm = this.formBuilder.group({
      savedActivityId: [''],
      savedActivityDetails: [''],
      savedActivityDate: [''],
      savedCoachId: [''],
      attachment: ['']
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
      console.log(this.submittedActivitiesArr);
    },
    (err) => console.log(err)
    );
  }

  // on click of edit button
  editSelectedActivities(index) {
    // this.savedActivityId = this.savedActivitiesArr[index].id;
    // this.savedActivityForm.controls['savedActivityId'].patchValue(this.savedActivityId);
    // this.savedActivityForm.value.savedActivityDate = this.savedActivitiesArr[index].dateOfActivity;
    // this.savedActivityForm.value.savedActivityDetails = this.savedActivitiesArr[index].description;
    // this.savedActivityForm.value.savedCoachId = this.savedActivitiesArr[index].teacherId;

    // console.log(this.savedActivityForm.value);
    console.log(this.savedActivitiesArr[index]);
    // console.log(this.editActivity);
    return this.editActivity = !this.editActivity;
  }

  // to get all activities of particular school
  getStudentActivity(schoolId) {
    this.studentService.getActivity(schoolId).subscribe(
      (res) => this.activities = res,
      (err) => console.log(err)
    );
  }

  // to get teacher/coach who perform selected activity
  getStudentCoach(activityId) {
    this.studentService.getCoach(this.schoolId, activityId).subscribe(
      (res) => this.coaches = res,
      (err) => console.log(err)
    );
  }

  // to UPDATE the saved activity
  updateActivity() {
  }

  onFileSelect(event) {  }

  // to SUBMIT the activity
  submitSavedActivity(index) {
    const activityPerformedId = this.savedActivitiesArr[index].id;
    this.studentService.submitActivity(activityPerformedId).subscribe((res) => {
      console.log(res);
      this.alertService.showSuccessToast('Activity Submitted !');
    },
      (err) => console.log(err)
    );
  }

}
