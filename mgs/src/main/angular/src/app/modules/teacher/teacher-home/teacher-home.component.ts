import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BASE_URL } from 'src/app/services/app.constant';

declare let $: any;

@Component({
  selector: 'app-teacher-home',
  templateUrl: './teacher-home.component.html',
  styleUrls: ['./teacher-home.component.scss']
})
export class TeacherHomeComponent implements OnInit {

  BASE_URL: string;
  allAssignedActi = []; // All assigned activities

  createSessionForm: FormGroup; // create session form
  teacherInfo: any;

  schoolGrades = [];
  startTime: any;
  endTime: any;

  sessionsArr = []; // Get Session of a week

  createSessionView = true;
  editSessionView = false;

  // Club details variable
  // clubId: any;
  assignedClubLoader = false;

  // files = []; // Array to store the attachment during create session
  path = "" // to Display the selected photos
  minDate = "" // Min Date to create session
  name = "" // Name of the File attached in Session

  // Variables to be used in Club Details
  showClubDetails:boolean = false;
  club_Obj: any;
  selectedClubId:any;

  constructor(private teacherService: TeacherService, private formBuilder: FormBuilder, private alertService: AlertService) {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
  }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getAllClubs();
    this.getSessionDetails();

    this.createSessionForm = this.formBuilder.group({
      id: [null],
      number: [, [Validators.required]],
      description: [, [Validators.required]],
      startDate: [, [Validators.required]],
      endDate: [],
      title: [, [Validators.required]],
      clubId: [, [Validators.required]],
      gradeIds: [, [Validators.required]],
      fileRequests: [null]
    });

  }

  //get list of assigned/supervised Clubs and Society
  getAllClubs() {
    this.assignedClubLoader = true;
    this.teacherService.getAssignedClubs().subscribe(res => {
      this.allAssignedActi = res;
      this.assignedClubLoader = false;
    }, (err) => {
      console.log(err);
      this.assignedClubLoader = false;
    });
  }

  // Grades of Supervisor for particular Club/Society
  clubGrades() {
    this.createSessionForm.controls['gradeIds'].reset();
    this.schoolGrades = [];
    const arr = this.allAssignedActi.filter(e => e.id == this.createSessionForm.value.clubId);
    this.schoolGrades = arr[0]['gradeResponses'];
  }

  // Create Session
  createSession() {
    const startDate = this.createSessionForm.value.startDate.split(" ")[0];
    this.createSessionForm.value.endDate = startDate + " " + this.endTime + ":00";
    this.createSessionForm.value.startDate = startDate + " " + this.startTime + ":00";

    if (this.createSessionView) {
      this.alertService.showLoader("");
      const formData = new FormData();
      Object.keys(this.createSessionForm.value).forEach(key => {
        if (key == 'gradeIds') {
          if (typeof (this.createSessionForm.value[key]) == 'object') {
            this.createSessionForm.value.gradeIds.forEach((element, index) => {
              formData.append(key + '[' + index + ']', element);
            });
          }
        }
        else if (key == 'fileRequests') {
          if (this.createSessionForm.value[key] !== null)
            formData.append(key + '[' + 0 + '].file', this.createSessionForm.value[key]);
        }
        else {
          formData.append(key, this.createSessionForm.value[key])
        }
      });

      this.teacherService.createNewSession(formData).subscribe((res) => {
        $('#createSessionModal').modal('hide');
        $('.modal-backdrop').remove();
        this.alertService.showMessageWithSym("Session Created !", "Success", "success");
        this.resetForm();
        this.getSessionDetails();
      }, (err) => {
        console.log(err);
        console.log("Error MSg: ", err.msg);
        this.createSessionForm.value.startDate = startDate;
        if (err.status === 500) {
          this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
        } else {
          this.alertService.showMessageWithSym("", "Error", "error");
        }

      });
    }

    if (this.editSessionView) {
      this.alertService.showLoader("");
      const formData = new FormData();
      Object.keys(this.createSessionForm.value).forEach(key => {
        if (key == 'gradeIds') {
          if (typeof (this.createSessionForm.value[key]) == 'object') {
            this.createSessionForm.value.gradeIds.forEach((element, index) => {
              formData.append(key + '[' + index + ']', element);
            });
          }
        }
        else if (key == 'fileRequests') {
          if (!(this.createSessionForm.value[key] == null)) {
            if (this.createSessionForm.value[key].id) {
              formData.append('fileRequests[' + 0 + '].id', this.createSessionForm.value[key].id);
            } else {
              formData.append(key + '[' + 0 + '].file', this.createSessionForm.value[key]);
            }
          }
        }
        else {
          formData.append(key, this.createSessionForm.value[key])
        }
      });

      this.teacherService.updateSession(formData).subscribe((res) => {
        $('#createSessionModal').modal('hide');
        $('.modal-backdrop').remove();
        this.alertService.showMessageWithSym("Session Edited !", "Success", "success");
        this.resetForm();
        this.getSessionDetails();
      }, (err) => {
        console.log(err);
        if (err.status == 400) {
          this.alertService.showMessageWithSym(err.msg, "Error", "error");
        }
        else {
          this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
        }
      });
    }
  }

  // Reset Form
  resetForm() {
    this.startTime = "";
    this.endTime = "";
    this.path = "";
    // this.files = [];
    this.name = "";
    this.schoolGrades = [];
    this.createSessionForm.reset();
  }

  // List of Sessions in current week
  getSessionDetails() {
    this.teacherService.getSession("week").subscribe((res) => {
      this.sessionsArr = res.sessions;
    }, (err) => { console.log(err); });
  }

  // Delete Scheduled Session
  deleteSession(session, out_index, in_index) {
    this.alertService.confirmWithoutLoader('question', 'Sure you want to DELETE ?', '', 'Yes').then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        this.teacherService.deleteSession(session.id).subscribe((res) => {
          this.sessionsArr[out_index].responses.splice(in_index, 1);
          this.alertService.showMessageWithSym("Session Deleted", "Success", "success");
          this.getSessionDetails();
        }, (err) => { console.log(err) });
      }

    });
  }

  createSessionBtn() {
    this.createSessionView = true;
    this.editSessionView = false;
    this.setMinDate();
  }

  // Edit/Update Current Session
  editSessionBtn(session) {
    $('#createSessionModal').modal('show');
    this.createSessionView = false;
    this.editSessionView = true;
    this.setMinDate();

    let sDate = new Date(session.startDate);
    let eDate = new Date(session.endDate);

    let sMinutes: any = sDate.getMinutes();
    let sHours: any = sDate.getHours();
    let eMinutes: any = eDate.getMinutes();
    let eHours: any = eDate.getHours();

    if (sHours < 10) {
      sHours = "0" + sHours;
    }
    if (sMinutes == 0) {
      sMinutes = "0" + sMinutes;
    }
    if (eHours < 10) {
      eHours = "0" + eHours;
    }
    if (eMinutes == 0) {
      eMinutes = "0" + eMinutes;
    }

    this.startTime = sHours + ":" + sMinutes;
    this.endTime = eHours + ":" + eMinutes;

    if (session.fileResponses.length) {
      this.name = session.fileResponses[0].name;
      this.path = BASE_URL + "/file/download?filePath=" + session.fileResponses[0].url;
    }

    this.createSessionForm.controls.startDate.patchValue(session.startDate.split(' ')[0]);
    this.createSessionForm.patchValue({
      id: session.id,
      number: session.number,
      title: session.title,
      clubId: session.club.id,
      description: session.description,
      fileRequests: session.fileResponses[0]
    });
    this.clubGrades();
    let arr = [];
    session.grades.forEach(element => {
      arr.push(element.id);
      this.createSessionForm.controls.gradeIds.setValue(arr);
    });
  }

  // Details of All Clubs and Societies in split window
  setClubDetails(val:boolean,clubObj?) {
    
    if(val){
      this.showClubDetails = true;
    } else {
      this.showClubDetails = false;
    }

    if (clubObj) {
      this.club_Obj = clubObj;
      this.selectedClubId = clubObj.id;
      // this.clubId = clubObj.id;
      // this.createSessionForm.controls.clubId.patchValue(this.clubId);
    }
  }

  // Adding Attachment at the time of create session
  onFileSelect(event) {
    this.name = ""; //reset the file name
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.createSessionForm.value.fileRequests = file;
      this.name = file.name;

      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);

      reader.onload = (event: any) => { this.path = event.target.result; }
    } else {
      this.path = null;
    }
  }

  // Remove the file from Session Schedule
  removeFile() {
    this.name = "";
    this.path = "";
    this.createSessionForm.value.fileRequests = null;
  }

  // Set Min Date
  setMinDate() {
    const minDate = new Date();
    let month: any = minDate.getMonth() + 1;
    let day: any = minDate.getDate();
    let year: any = minDate.getFullYear();

    if (month < 10)
      month = '0' + month.toString();
    if (day < 10)
      day = '0' + day.toString();
    this.minDate = [year, month, day].join('-');
  }
}