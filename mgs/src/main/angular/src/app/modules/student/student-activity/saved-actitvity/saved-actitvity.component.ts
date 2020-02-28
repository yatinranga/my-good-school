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
<<<<<<< HEAD
  editActivity = false;
  savedActivityForm: FormGroup;

  activityName : any;
=======
  editActivityShow = false;
  savedActivityForm: FormGroup;

  activityName: any;
>>>>>>> 1d5a946f9df74b387b7e9563590129ff71cefdc0
  activityDetails: any;
  activityTeacher: any;
  activityId: any;
  activityDate: any;
  file = [];
  url = '';
  activityType = "All";

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.schoolId = this.studentInfo['student'].schoolId;

    // this.getStudentSavedActivities(this.studentInfo['student'].id)
    this.getStudentSavedActivities(this.studentId);
    this.getStudentSubmittedActivities(this.studentId);
    this.getStudentAllActivities(this.studentId);

    // this.getStudentActivity(this.schoolId);

    this.savedActivityForm = this.formBuilder.group({
      savedActivityName: [''],
<<<<<<< HEAD
      savedActivityId: [''],
      savedActivityDetails: [''],
      savedActivityDate: [''],
      savedCoachId: [''],
      attachment: ['']
    });
=======
      activityId: [''],
      description: [''],
      dateOfActivity: [''],
      coachId: [''],
      attachment: [''],
      id: ['']
    })
>>>>>>> 1d5a946f9df74b387b7e9563590129ff71cefdc0
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
    },
      (err) => console.log(err)
    );
  }

  // on click of edit button
  editSavedActivity(activity) {
    // this.activityName = activity.activityName;
    // this.activityTeacher = activity.teacherName;

    const date = this.getDate(activity.dateOfActivity);
    if ((date.getMonth() + 1) < 10) {
      if (date.getDate() < 10) {
        this.savedActivityForm.controls['dateOfActivity'].patchValue(date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)) + '-' + ('0' + date.getDate()));
      } else {
        this.savedActivityForm.controls['dateOfActivity'].patchValue(date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)) + '-' + date.getDate());
      }
    } else {
      if (date.getDate() < 10) {
        this.savedActivityForm.controls['dateOfActivity'].patchValue(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + ('0' + date.getDate()));
      } else {
        this.savedActivityForm.controls['dateOfActivity'].patchValue(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate());
      }
    }

    this.savedActivityForm.patchValue({
      activityId: activity.activityId,
      description: activity.description,
      id: activity.id,
    })
    console.log(this.savedActivityForm.value);

    this.getStudentActivity(this.schoolId);
    return this.editActivityShow = true;
  }

  // to SUBMIT the activity
  submitSavedActivity(index) {
    const activityId = this.savedActivitiesArr[index].id;
    this.studentService.submitActivity(activityId).subscribe((res) => {
      console.log(res);
      this.alertService.showSuccessToast('Activity Submitted !');
      // splice the selected activity from savedActivity array and push it into submit activity array
    },
      (err) => console.log(err)
    );
  }

  deleteSavedActivity(activity) {
    const activityId = activity.id;
    console.log(activityId);
    this.studentService.deleteActivity(activityId).subscribe((res) => {
      console.log(res);
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
<<<<<<< HEAD

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
    const activityPerformedId = this.savedActivitiesArr[index].id;
    this.studentService.submitActivity(activityPerformedId).subscribe((res) => {
      console.log(res);
      this.alertService.showSuccessToast('Activity Submitted !');
    },
=======
    if (event.target.files.length > 0) {
      this.file = [...event.target.files];
      this.savedActivityForm.value['attachment'] = this.file;
      console.log(this.file);
    }
  }

  // to UPDATE the saved activity
  updateActivity() {
    const formData = new FormData();
    let date = new Date(this.savedActivityForm.value.dateOfActivity);
    let activityDate = date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate();

    formData.append('studentId', this.studentInfo['student'].id);
    formData.append('activityId', this.savedActivityForm.value.activityId);
    formData.append('coachId', this.savedActivityForm.value.coachId);
    formData.append('dateOfActivity', activityDate);
    formData.append('description', this.savedActivityForm.value.description);
    formData.append('id', this.savedActivityForm.value.id)

    if (this.savedActivityForm.value.attachment.length > 0) {
      this.savedActivityForm.value.attachment.forEach((element, index) => {
        formData.append('fileRequests[' + index + '].file', element);
      });
    }

    this.studentService.addActivity("/api/students/activities", formData).subscribe(
      (res) => {
        console.log(res);
        this.alertService.showSuccessToast('Activity Updated !');
      },
>>>>>>> 1d5a946f9df74b387b7e9563590129ff71cefdc0
      (err) => console.log(err)
    );
  }

  activityView(event) {
    this.activityType = event;
  }

  getDate(date) {
    return new Date(date)
  }

}
