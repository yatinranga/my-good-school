import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
declare let $: any;

@Component({
  selector: 'app-saved-actitvity',
  templateUrl: './saved-actitvity.component.html',
  styleUrls: ['./saved-actitvity.component.scss']
})
export class SavedActitvityComponent implements OnInit {


  maxDate: string;
  minDate: string;
  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

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


  activityId: "";
  activityDate: any;
  files = [];

  activityType = 'All';
  fourS: any = '';
  psdAreas: any = '';
  focusAreas: any = '';

  loader = false;
  modal_loader = false;
  submit_loader = false;

  copySavedActi: any = []; // used for filter activity
  copysubmitActi: any = []; // used for filter activity
  copyAllActi: any = []; // used for filter activity
  copyReviewActi: any = []; // used for filter activity

  activitiesArr = []; // single arr for performed actvities
  order = false;
  count = 0; // to count the number of words enter
  index: number // used when Acti_Status  =  saved to update the table.

  ngOnInit() {
    this.setMinDate();
    this.setMaxDate();
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo.id;
    this.schoolId = this.studentInfo.schoolId;
    this.activityView(this.activityType);
    this.getStudentActivity();
    this.getAreas(); // to get PSD Areas, Focus Area and 4s 

    this.savedActivityForm = this.formBuilder.group({
      activityId: [, [Validators.required]],
      description: [{ value: '', disabled: true }, [Validators.required, Validators.minLength(50)]],
      dateOfActivity: [{ value: '', disabled: true }, [Validators.required]],
      coachId: [{ value: '', disabled: true }, [Validators.required]],
      title: [{ value: '', disabled: true }, [Validators.required]],
      attachment: [],
      id: []
    });
  }

  // get PSD , Focus Area and 4S
  getAreas() {
    this.studentService.getActivityAreas().subscribe((res) => {
      this.psdAreaArr = res["PSD Areas"]
      this.focusAreaArr = res["Focus Areas"]
      this.fourSArr = res["Four S"]
    },
      (err) => { console.log(err); });
  }

  // to get the list of SAVED Activities of student
  getStudentSavedActivities(studentId) {
    this.studentService.getSavedActivity(studentId).subscribe((res) => {
      console.log(res);
      this.savedActivitiesArr = res;
      this.copySavedActi = Object.assign([], res);
      this.activitiesArr = this.savedActivitiesArr;
      this.loader = false;
      this.filterActivities();
    }, (err) => {
      this.loader = false;
      console.log(err);
    });
  }

  // to get the list of SUBMITTED Activities of student
  getStudentSubmittedActivities(studentId) {
    this.studentService.getSubmittedActivity(studentId).subscribe((res) => {
      this.submittedActivitiesArr = res;
      this.copysubmitActi = Object.assign([], res);
      this.activitiesArr = this.submittedActivitiesArr;
      this.loader = false;
      this.filterActivities();
    }, (err) => {
      this.loader = false;
      console.log(err);
    });
  }

  // to get the list of ALL Activities of student
  getStudentAllActivities(studentId) {
    this.studentService.getAllActivity(studentId).subscribe((res) => {
      this.allActivitiesArr = res;
      this.copyAllActi = Object.assign([], res);
      this.activitiesArr = this.allActivitiesArr;
      this.loader = false;
      this.filterActivities();
    }, (err) => {
      this.loader = false;
      console.log(err);
    }
    );
  }

  // to get the list of REVIEWED Activities of student
  getStudentReviewedActivities(studentId) {
    this.studentService.getReviewedActivity(studentId).subscribe((res) => {
      this.reviewedActivitiesArr = res;
      this.copyReviewActi = Object.assign([], res);
      this.activitiesArr = this.reviewedActivitiesArr;
      this.loader = false;
      this.filterActivities();
    }, (err) => {
      this.loader = false;
      console.log(err);
    });

  }

  // on click of edit button
  editSavedActivity(e, activity, index) {
    this.index = index;
    $('#addActivityModal').modal('show');
    $('#addActivityModal').modal({
      backdrop: 'static',
      keyboard: false
    });

    e.stopPropagation();
    console.log(activity);
    this.activityId = activity.activityId;
    this.getStudentCoach(this.activityId);
    this.savedActivityForm.controls.dateOfActivity.patchValue(activity.dateOfActivity.split(' ')[0]);
    this.savedActivityForm.patchValue({
      activityId: activity.activityId,
      description: activity.description,
      coachId: activity.coachId,
      title: activity.title,
      id: activity.id,
    });
    this.files = activity.fileResponses;
    this.editActivityShow = true;
    this.addActivityShow = false;

  }

  addActivity() {
    console.log("Add Activity Modal");
    this.savedActivityForm.reset();
    this.savedActivityForm.value.attachment = [];
    this.files = [];
    this.addActivityShow = true;
    this.editActivityShow = false;
    // $('#addActivityModal').modal({
    //   backdrop: 'static',
    //   keyboard: false
    // });
    // $('#addActivityModal').modal('show');
  }

  // to SUBMIT the activity
  submitSavedActivity(e, index, array) {
    e.stopPropagation();
    this.alertService.confirmWithoutLoader('question', 'Once submitted you will not be able to edit,\nSure you want to submit?', '', 'Confirm').then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        const activityId = array[index].id;
        this.studentService.submitActivity(activityId).subscribe((res) => {
          console.log(res);
          if (this.activityType === 'All') {
            array.splice(index, 1);
            array.unshift(res);
            // array[index].activityStatus = 'SubmittedByStudent';
          } else {
            array.splice(index, 1);
          }
          this.alertService.showSuccessAlert('Activity Submitted !');
        }, (err) => {
          console.log(err);
        });

      }
    });

  }

  // Direct Submit Activity at the time of Add/Edit Activity
  // directSubmitActivity(activityId) {
  //   this.alertService.confirmWithoutLoader('question', 'Do you want to submit ?', '', 'Yes').then(result => {
  //     console.log(result);
  //     if (result.value) {
  //       this.studentService.submitActivity(activityId).subscribe((res) => {
  //         console.log(res);
  //         if (this.activityType === 'All') {
  //           this.activitiesArr.shift();
  //           this.activitiesArr.unshift(res);
  //         } else {
  //           this.activitiesArr.shift();
  //         }
  //         this.alertService.showSuccessAlert('Activity Submitted !');
  //       }, (err) => {
  //         console.log(err);
  //       });
  //     }
  //   });
  // }

  // DELETE Activity
  deleteSavedActivity(e, activity, i) {
    e.stopPropagation();
    this.alertService.confirmWithoutLoader('question', 'Are you sure you want to delete ?', '', 'Yes').then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        const activityId = activity.id;
        console.log(activityId);
        this.studentService.deleteActivity(activityId).subscribe((res) => {
          console.log(res);
          this.activitiesArr.splice(i, 1);
          this.alertService.showSuccessToast('Activity Deleted !');
        },
          (err) => console.log(err));
      }
    })
  }


  // to get all activities of particular school
  getStudentActivity() {
    this.studentService.getAllEnrolledClub().subscribe((res) => {
      this.activities = res;
    },
      (err) => console.log(err)
    );
  }

  // to get teacher/coach who perform selected activity
  getStudentCoach(activityId) {
    this.savedActivityForm.value.activityId = activityId;
    this.coaches = [];
    if (activityId != null) {
      this.modal_loader = true;
      this.studentService.getCoach(this.schoolId, activityId).subscribe((res) => {
        this.coaches = res;
        if (this.coaches.length) {
          this.savedActivityForm.get('coachId').enable();
          this.savedActivityForm.get('description').enable();
          this.savedActivityForm.get('dateOfActivity').enable();
          this.savedActivityForm.get('title').enable();
        } else {
          this.savedActivityForm.get('coachId').disable();
          this.savedActivityForm.get('description').disable();
          this.savedActivityForm.get('dateOfActivity').disable();
          this.savedActivityForm.get('title').disable();
        }
        this.modal_loader = false;
      },
        (err) => {
          console.log(err);
          this.modal_loader = false;
        }
      );
    }
  }

  // to UPDATE the saved activity
  updateActivity() {

    if (this.editActivityShow) {
      this.submit_loader = true;
      this.savedActivityForm.value.attachment = this.files;
      console.log(this.savedActivityForm.value.attachment);
      const formData = new FormData();
      const activityDate = this.savedActivityForm.value.dateOfActivity + ' 00:00:00';
      // const activityDate = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate();

      formData.append('studentId', this.studentInfo.id);
      formData.append('activityId', this.savedActivityForm.value.activityId);
      formData.append('coachId', this.savedActivityForm.value.coachId);
      formData.append('dateOfActivity', activityDate);
      formData.append('description', this.savedActivityForm.value.description);
      formData.append('title', this.savedActivityForm.value.title);
      formData.append('id', this.savedActivityForm.value.id);

      if (this.savedActivityForm.value.attachment.length > 0) {
        this.savedActivityForm.value.attachment.forEach((element, index) => {
          if (element.id) {
            console.log("Old file");
            formData.append('fileRequests[' + index + '].id', element.id);
          } else {
            console.log("New file");
            formData.append('fileRequests[' + index + '].file', element);
          }
        });
      }
      console.log(this.savedActivityForm.value);

      this.studentService.addActivity('/api/student/activity', formData).subscribe(
        (res) => {
          console.log(res);
          this.activitiesArr.splice(this.index, 1);
          this.activitiesArr.unshift(res);
          this.submit_loader = false;
          $('#addActivityModal').modal('hide');
          $('.modal-backdrop').remove();
          this.alertService.showSuccessToast('Activity Updated !');
          this.editActivityShow = false;
        },
        (err) => {
          this.submit_loader = false;
          console.log(err);
          if (err.status == 400) {
            this.alertService.showMessageWithSym(err.msg, "", "info");
          }
          else {
            this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
          }
          // $('#addActivityModal').modal('hide');
          // $('.modal-backdrop').remove();
        }
      );
    }

    if (this.addActivityShow) {
      this.submit_loader = true;
      this.savedActivityForm.value.attachment = this.files;
      console.log(this.savedActivityForm.value.attachment);
      const time = this.savedActivityForm.value.dateOfActivity + ' 00:00:00';
      this.savedActivityForm.value.dateOfActivity = time;
      const formData = new FormData();
      formData.append('studentId', this.studentInfo.id);
      formData.append('activityId', this.savedActivityForm.value.activityId);
      formData.append('coachId', this.savedActivityForm.value.coachId);
      formData.append('dateOfActivity', time);
      formData.append('description', this.savedActivityForm.value.description);
      formData.append('title', this.savedActivityForm.value.title);

      if (this.savedActivityForm.value.attachment.length > 0) {
        this.savedActivityForm.value.attachment.forEach((element, index) => {
          formData.append('fileRequests[' + index + '].file', element);
        });
      }

      console.log(this.savedActivityForm.value);

      this.studentService.addActivity('/api/student/activity', formData).subscribe(
        (res) => {
          console.log(res);
          this.activitiesArr.unshift(res);
          this.submit_loader = false;
          $('#addActivityModal').modal('hide');
          $('.modal-backdrop').remove();
          this.alertService.showSuccessToast('Activity Saved');
          // this.alertService.showSuccessToast('Activity Saved !').then((response) => {
          //   this.directSubmitActivity(res.id);
          // });
          this.addActivityShow = false;
        },
        (err) => {
          this.submit_loader = false;
          console.log(err);
          if (err.status == 400) {
            this.alertService.showMessageWithSym(err.msg, "", "info");
          }
          else {
            this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
          }
          // $('#addActivityModal').modal('hide');
          // $('.modal-backdrop').remove();
        }
      );
    }

  }

  onFileSelect(event) {
    if (event.target.files.length <= 5 && (this.files.length + event.target.files.length) <= 5) {
      if (event.target.files.length > 0) {
        this.files = [...this.files, ...event.target.files];
        console.log(this.files.length);
        console.log(this.files);
        // this.savedActivityForm.value.attachment = this.files;
        // console.log(this.files);
      }
    } else {
      console.log("error");
      this.alertService.showErrorAlert("Cannot select files more than 5");
    }
  }

  // call API for Saved , Submitted , Reviewed and All Activities
  activityView(event) {
    this.activityType = event;
    this.loader = true;
    this.activitiesArr = [];
    switch (this.activityType) {
      case 'All': {
        this.getStudentAllActivities(this.studentId);
        break;
      }

      case 'Saved': {
        this.getStudentSavedActivities(this.studentId);
        break;
      }

      case 'Reviewed': {
        this.getStudentReviewedActivities(this.studentId);
        break;
      }

      case 'Submitted': {
        this.getStudentSubmittedActivities(this.studentId);
        break;
      }
    }

  }

  // Filter Activities on the basis of PSD , Focus Area and 4S
  filterActivities = () => {
    switch (this.activityType) {
      case 'All': {
        this.activitiesArr = this.filter(Object.assign([], this.copyAllActi));
        break;
      }

      case 'Saved': {
        this.activitiesArr = this.filter(Object.assign([], this.copySavedActi));
        break;
      }

      case 'Reviewed': {
        this.activitiesArr = this.filter(Object.assign([], this.copyReviewActi));
        break;
      }

      case 'Submitted': {
        this.activitiesArr = this.filter(Object.assign([], this.copysubmitActi));
        break;
      }
    }
  }

  // Actual Filtering on the basis of PSD , Focus Area and 4S
  filter(array: any[]) {
    let filterActivitiesArr = [];
    if (this.psdAreas && this.fourS && this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.fourS === this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas));
    } else if (this.psdAreas && this.fourS) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.fourS === this.fourS);
    } else if (this.fourS && this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.fourS === this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas));
    } else if (this.psdAreas && this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.focusAreas && e.focusAreas.includes(this.focusAreas));
    } else if (this.psdAreas) {
      filterActivitiesArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas));
    } else if (this.fourS) {
      filterActivitiesArr = array.filter(e => e.fourS === this.fourS);
    } else if (this.focusAreas) {
      filterActivitiesArr = array.filter(e => e.focusAreas && e.focusAreas.includes(this.focusAreas));
    } else {

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

  // Sorting Activities
  sortByStatus() {
    this.order = !this.order;
    // sort by activityStatus
    this.activitiesArr.sort((a, b) => {
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

  // to reset the Add/Edit Form
  resetForm() {
    this.savedActivityForm.reset();
    this.files = [];
  }

  // to DOWNLOAD the Attachments
  downloadFile(url) {
    this.studentService.downloadAttachment(url).subscribe((res) => {
      console.log(res);
    }, (err) => { console.log(err) });
  }

  setMinDate() {
    const minDate = new Date();
    minDate.setDate(minDate.getDate() - 30);
    let month: any = minDate.getMonth() + 1;
    let day: any = minDate.getDate();
    let year: any = minDate.getFullYear();

    if (month < 10)
      month = '0' + month.toString();
    if (day < 10)
      day = '0' + day.toString();
    this.minDate = [year, month, day].join('-');
  }

  setMaxDate() {
    const minDate = new Date();
    let month: any = minDate.getMonth() + 1;
    let day: any = minDate.getDate();
    let year: any = minDate.getFullYear();

    if (month < 10)
      month = '0' + month.toString();
    if (day < 10)
      day = '0' + day.toString();
    this.maxDate = [year, month, day].join('-');
  }

  // stop toogle of table
  stopCollapse(e) {
    e.stopPropagation();
  }

  // to count the number of words 
  wordCount(e) {
    if (e) {
      this.count = e.split(/\s\w/).length;
    } else {
      this.count = 0;
    }
  }

}