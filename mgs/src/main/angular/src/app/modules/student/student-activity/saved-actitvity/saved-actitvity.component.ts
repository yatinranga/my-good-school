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
  submittedActivitiesArr = [];
  reviewedActivitiesArr = [];
  allActivitiesArr = [];
  activities = [];
  coaches = [];
  schoolId: any;
  studentId : any;
  editActivity = false;
  savedActivityForm: FormGroup;
  
  activityName : any;
  activityDetails: any;
  activityTeacher: any;
  activityId: any;
  file = [];
  url = '';
  activityType = "Saved";

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id
    this.schoolId = this.studentInfo['student'].schoolId;
    
    // this.getStudentSavedActivities(this.studentInfo['student'].id)
    this.getStudentSavedActivities(this.studentId);
    this.getStudentSubmittedActivities(this.studentId);
    this.getStudentAllActivities(this.studentId);
    
    // this.getStudentActivity(this.schoolId);

    this.savedActivityForm = this.formBuilder.group({
      savedActivityName: [''],
      savedActivityId: [''],
      savedActivityDetails: [''],
      savedActivityDate: [''],
      savedCoachId: [''],
      attachment: ['']
    })
  }

  // to get the list of SAVED Activities of student
  getStudentSavedActivities(studentId) {
    this.studentService.getSavedActivity(studentId).subscribe((res) => {
      this.savedActivitiesArr = res;
    },
      (err) => console.log(err));
  }
  // to get the list of SUBMITTED Activities of student
  getStudentSubmittedActivities(studentId){
    this.studentService.getSubmittedActivity(studentId).subscribe((res) => {
      this.submittedActivitiesArr = res;
    },
    (err) => console.log(err)
    );
  }

  // to get the list of ALL Activities of student
  getStudentAllActivities(studentId){
    this.studentService.getAllActivity(studentId).subscribe((res) => {
      this.allActivitiesArr = res;
    },
    (err) => console.log(err)
    );
  }

  // on click of edit button
  editSelectedActivities(index) {
    this.activityName = this.savedActivitiesArr[index]['activityName'];
    this.activityDetails = this.savedActivitiesArr[index]['description'];
    this.activityTeacher = this.savedActivitiesArr[index]['teacherName'];
    this.activityId = this.savedActivitiesArr[index]['activityId'];

    this.getStudentActivity(this.schoolId);

    console.log(this.savedActivitiesArr[index]);
    return this.editActivity = !this.editActivity;
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
      console.log(res);
    },
      (err) => console.log(err)
    );
  }

  // to UPDATE the saved activity
  updateActivity() {
    console.log(this.savedActivityForm.value);
  }

  onFileSelect(event) {
    
      var reader = new FileReader();
      this.file = [...event.target.files];
      this.file.forEach((element,index) => console.log(this.file[index]));

      reader.readAsDataURL(event.target.files[0]); // read file as data url

      reader.onload = (event) => { // called once readAsDataURL is completed
        // this.url = event.target.result;
      }
    
    // if (event.target.files.length > 0) {
    //   this.file = [...event.target.files];
    //   console.log(this.file)
    //   this.file.forEach((element,index) => console.log(this.file[index]['name']))
    // }
  }

  // to SUBMIT the activity
  submitSavedActivity(index) {
    let activityPerformedId = this.savedActivitiesArr[index].id;
    this.studentService.submitActivity(activityPerformedId).subscribe((res) => {
      console.log(res);
      this.alertService.showSuccessToast('Activity Submitted !');
    },
      (err) => console.log(err)
    );
  }

  activityView(event){
      this.activityType = event;
  }

  getDate(date){
    return new Date(date)
  }

}