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

  psdAreaArr = [];
  focusAreaArr = [];
  fourSArr = [];
  schoolId: any;
  studentId: any;
  editActivityShow = false;
  savedActivityForm: FormGroup;


  activityId: any;
  activityDate: any;
  files = [];
  url = '';

  activityType = 'All';
  fourS: any = "";
  psdAreas: any = "";
  focusAreas: any = "";

  loader: boolean = false;
  modal_loader = false;
  submit_loader = false;

  copySavedActi: any = [];
  copysubmitActi: any = [];
  copyAllActi: any = [];
  copyReviewActi: any = [];


  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.schoolId = this.studentInfo['student'].schoolId;
    this.activityView(this.activityType);
    this.getStudentActivity(this.schoolId);
    this.getAreas(); // to get PSD Areas, Focus Area and 4s 

    this.savedActivityForm = this.formBuilder.group({
      activityId: [''],
      description: [''],
      dateOfActivity: [''],
      coachId: [''],
      attachment: [],
      id: ['']
    });
  }

  // get PSD , Focus Area and 4S
  getAreas() {
    this.studentService.getFocusAreas().subscribe((res) => {
      this.focusAreaArr = res;
    },
      (err) => { console.log(err) });

    this.studentService.getPsdAreas().subscribe((res) => {
      this.psdAreaArr = res;
    },
      (err) => { console.log(err) });

    this.studentService.getFourS().subscribe((res) => {
      this.fourSArr = res;
    },
      (err) => { console.log(err) });
  }

  // to get the list of SAVED Activities of student
  getStudentSavedActivities(studentId) {
    this.studentService.getSavedActivity(studentId).subscribe((res) => {
      this.savedActivitiesArr = res;
      this.copySavedActi = Object.assign([], res);
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
      this.copysubmitActi = Object.assign([], res);
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
      this.copyAllActi = Object.assign([], res);
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
      this.copyReviewActi = Object.assign([], res);
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
    this.files = [];
    this.addActivityShow = true;
    this.editActivityShow = false;
  }

  // to SUBMIT the activity
  submitSavedActivity(e, index, array) {
    debugger
    const submit = confirm("Do you want to submit ?");
    if (submit) {
      e.stopPropagation();
      const activityId = array[index].id;
      this.studentService.submitActivity(activityId).subscribe((res) => {
        console.log(res);
        if (this.activityType == 'All')
          array[index].activityStatus = 'SubmittedByStudent';
        else
          array.splice(index, 1);
        this.alertService.showSuccessAlert('Activity Submitted !');
      }, (err) => {
        console.log(err)
      });
    }
  }

  // DELETE Activity
  deleteSavedActivity(e, activity, i) {
    const submit = confirm("Do you want to delete ?");
    if (submit) {
      e.stopPropagation();
      const activityId = activity.id;
      console.log(activityId);
      this.studentService.deleteActivity(activityId).subscribe((res) => {
        console.log(res);
        if (this.activityType == 'All')
          this.allActivitiesArr.splice(i, 1);
        else
          this.savedActivitiesArr.splice(i, 1);
        this.alertService.showSuccessToast('Activity Deleted !');
      },
        (err) => console.log(err));
    }
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
    this.modal_loader = true;
    this.studentService.getCoach(this.schoolId, activityId).subscribe((res) => {
      this.coaches = res;
      this.modal_loader = false;
    },
      (err) => {
        console.log(err);
        this.modal_loader = false;
      }
    );
  }

  // to UPDATE the saved activity
  updateActivity() {
    // const submit = confirm("Do you want to Submit ?");
    // if(submit) {

    // }
    // else {    }

    if (this.editActivityShow) {
      this.submit_loader = true;
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
          this.submit_loader = false;
          this.alertService.showSuccessToast('Activity Updated !');
          this.editActivityShow = false;
        },
        (err) => {
          this.submit_loader = false;
          console.log(err)
        }
      );
    }

    if (this.addActivityShow) {
      this.submit_loader = true;
      this.savedActivityForm.value.attachment = this.files;
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
          this.savedActivitiesArr.unshift(res);
          this.allActivitiesArr.unshift(res);
          this.submit_loader = false;
          this.alertService.showSuccessToast('Activity Saved !');
          this.addActivityShow = false;
        },
        (err) => {
          this.submit_loader = false;
          console.log(err)
        }
      );
    }

  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.files = [...event.target.files];
      // this.savedActivityForm.value.attachment = this.files;
      // console.log(this.files);
    }
  }

  // call API for Saved , Submitted , Reviewed and All Activities
  activityView(event) {
    this.activityType = event;
    this.loader = true;
    switch (this.activityType) {
      case "All": {
        this.getStudentAllActivities(this.studentId);
        break;
      }

      case "Saved": {
        this.getStudentSavedActivities(this.studentId);
        break;
      }

      case "Reviewed": {
        this.getStudentReviewedActivities(this.studentId);
        break;
      }

      case "Submitted": {
        this.getStudentSubmittedActivities(this.studentId);
        break;
      }
    }
    this.filterActivities();
  }

  // Filter Activities on the basis of PSD , Focus Area and 4S
  filterActivities = () => {
    switch (this.activityType) {
      case "All": {
        this.allActivitiesArr = this.filter(Object.assign([], this.copyAllActi))
        break;
      }

      case "Saved": {
        this.savedActivitiesArr = this.filter(Object.assign([], this.copySavedActi))
        break;
      }

      case "Reviewed": {
        this.reviewedActivitiesArr = this.filter(Object.assign([], this.copyReviewActi))
        break;
      }

      case "Submitted": {
        this.submittedActivitiesArr = this.filter(Object.assign([], this.copysubmitActi))
        break;
      }
    }
  }

  filter(array: any[]) {
    let filterActivitiesArr = [];
    if (this.psdAreas && this.fourS && this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.fourS == this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas))
    }
    else if (this.psdAreas && this.fourS) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.fourS == this.fourS)
    }
    else if (this.fourS && this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.fourS == this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas))
    }
    else if (this.psdAreas && this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.focusAreas && e.focusAreas.includes(this.focusAreas))
    }
    else if (this.psdAreas) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas))
    }
    else if (this.fourS) {
      filterActivitiesArr = array.filter(e => e.fourS == this.fourS)
    }
    else if (this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.focusAreas && e.focusAreas.includes(this.focusAreas))
    }
    else {

      filterActivitiesArr = array;
    }

    return filterActivitiesArr;

  }

  getDate(date) {
    return new Date(date);
  }

  onCancel() {
    this.editActivityShow = false;
    this.addActivityShow = false;
  }

  removeFile(index: number) {
    this.files.splice(index, 1);
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

}