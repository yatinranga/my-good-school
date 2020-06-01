import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { AlertService } from 'src/app/services/alert.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
declare let $: any;

@Component({
  selector: 'app-teacher-home',
  templateUrl: './teacher-home.component.html',
  styleUrls: ['./teacher-home.component.scss']
})
export class TeacherHomeComponent implements OnInit {

  assignedClubsArr = [];
  assignedSocietyArr = [];
  allAssignedActi = []; // All assigned activities

  createSessionForm: FormGroup; // create session form
  teacherInfo: any;
  schoolId: any;
  teacherId: any;

  schoolGrades = [];
  startTime: any;
  endTime: any;

  sessionsArr = []; // Get Session of a week
  createSessionShow = false;
  teacherName: any;

  createSessionView = true;
  editSessionView = false;

  // Club details variable
  clubId: any;
  clubName = "";
  filterVal = "";
  filterReqVal = "";
  clubSchedule = [];
  copySchedule = [];
  studentsArr = [];
  copyStudentArr = [];
  clubReqArr = [];
  copyClubReqArr = [];
  clubSch_loader = false;
  stu_loader = false;
  clubReqLoader = false;
  gradeId = "";

  // files = []; // Array to store the attachment during create session
  path = "" // to Display the selected photos
  minDate = "" // Min Date to create session
  name = "" // Name of the File attached in Session

  constructor(private teacherService: TeacherService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.schoolId = this.teacherInfo.schoolId;
    this.teacherName = this.teacherInfo.name;
    this.teacherId = this.teacherInfo.id;
    this.clubReqLoader = true;
    this.getAllClubs();
    this.getSessionDetails();
    // this.getAllClubReq();

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

    this.getSchoolGrades(this.schoolId);
  }

  //get list of assigned/supervised Clubs and Society
  getAllClubs() {
    this.teacherService.getAssignedClubs().subscribe(res => {
      this.assignedClubsArr = res.filter((e) => (e.clubOrSociety == "Club"));
      this.assignedSocietyArr = res.filter((e) => (e.clubOrSociety == "Society"));
      this.allAssignedActi = res;
    }, (err) => { console.log(err); });
  }

  // get all the Request of Clubs and Society
  // getAllClubReq() {
  //   this.teacherService.getClubReq().subscribe(res => {
  //     this.clubReqArr = res;
  //     this.clubReqLoader = false;
  //   }, (err) => {
  //     console.log(err);
  //     this.clubReqLoader = false;
  //   });
  // }

  setClubReq(obj, index, verify) {

    // To Verify/Approve the Club/Society Request
    if (verify == 'true') {
      this.alertService.confirmWithoutLoader('question', 'Sure you want to Approve ?', '', 'Yes').then(result => {
        if (result.value) {
          this.alertService.showLoader("");
          this.teacherService.approveClubReq(obj.student.id, obj.club.id, verify).subscribe(res => {
            this.alertService.showSuccessAlert("Request Approved!");
            this.clubReqArr.splice(index, 1);
            // this.clubReqArr.unshift(res);
            this.getClubRequests(this.clubId);
            this.getClubStudents(this.clubId, this.teacherId);
          }, (err) => { console.log(err); })
        }
      })
    }

    // To Reject the Club/Society Request
    if (verify == 'false') {
      this.alertService.confirmWithoutLoader('question', 'Sure you want to Reject ?', '', 'Yes').then(result => {
        if (result.value) {
          this.alertService.showLoader("");
          this.teacherService.approveClubReq(obj.student.id, obj.club.id, verify).subscribe(res => {
            this.alertService.showSuccessAlert("Request Rejected!");
            this.clubReqArr.splice(index, 1);
            // this.clubReqArr.unshift(res);
            this.getClubRequests(this.clubId);

          }, (err) => { console.log(err) })
        }
      })
    }

  }

  // get List of School Grades 
  getSchoolGrades(schoolId) {
    this.teacherService.getGrades(schoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
      (err) => console.log(err));
  }

  // Create Session
  createSession() {
    const startDate = this.createSessionForm.value.startDate.split(" ")[0];
    this.createSessionForm.value.endDate = startDate + " " + this.endTime + ":00";
    this.createSessionForm.value.startDate = startDate + " " + this.startTime + ":00";
    console.log(this.createSessionForm.value);

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
        console.log(res);
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
        console.log(res);
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
    this.createSessionForm.reset();
  }

  // List of Sessions in current week
  getSessionDetails() {
    this.teacherService.getSession("week").subscribe((res) => {
      console.log(res.sessions);
      this.sessionsArr = res.sessions;
    }, (err) => { console.log(err); });
  }

  // Delete Scheduled Session
  deleteSession(session, out_index, in_index) {
    console.log(session);
    this.alertService.confirmWithoutLoader('question', 'Sure you want to DELETE ?', '', 'Yes').then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        this.teacherService.deleteSession(session.id).subscribe((res) => {
          console.log(res);
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
    console.log(session);
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
      this.path = session.fileResponses[0].url;
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

    let arr = [];
    session.grades.forEach(element => {
      arr.push(element.id);
      this.createSessionForm.controls.gradeIds.setValue(arr);
    });
  }

  // Details of All Clubs and Societies
  clubDetails(clubObj) {
    // $('#clubDetailsModal').modal('show');
    localStorage.setItem('club', JSON.stringify(clubObj));
    this.clubSchedule = [];
    this.studentsArr = [];
    this.clubReqArr = [];
    this.filterVal = ""; // Reset Filter value
    this.filterReqVal = "";
    this.gradeId = "";
    this.clubSch_loader = true;
    this.stu_loader = true;
    this.clubReqLoader = true;
    this.clubName = clubObj.name;
    this.clubId = clubObj.id;
    this.getClubRequests(clubObj.id);
    this.getClubStudents(clubObj.id, this.teacherId);
    this.getClubSession(clubObj.id);
    this.createSessionForm.controls.clubId.patchValue(this.clubId);
  }

  // get Session of a particualr Club/Society
  getClubSession(clubId) {
    this.teacherService.getSupervisedClubSession(clubId).subscribe((res) => {
      this.clubSchedule = res.sessions;
      this.copySchedule = Object.assign([], res.sessions);
      this.clubSch_loader = false;
    }, (err) => {
      console.log(err);
      this.clubSch_loader = false;
    });
  }

  // Filter session on the bases of ALL, UPCOMING and ENDED
  filterSession(val) {
    this.clubSchedule = this.filter(Object.assign([], this.copySchedule), val, "Session");
  }

  // List of Students of the particular Club
  getClubStudents(clubId, teacherId) {
    this.teacherService.getSupervisorClubStudents(clubId, teacherId).subscribe((res) => {
      this.studentsArr = res;
      this.copyStudentArr = Object.assign([], res);
      this.stu_loader = false;
    }, (err) => {
      console.log(err);
      this.stu_loader = false;
    });

  }

  // Filter student on the bases of grade
  filterStudent(val) {
    this.studentsArr = this.filter(Object.assign([], this.copyStudentArr), val, "Student");
  }

  // Requests of a particular club/society
  getClubRequests(clubId) {
    this.teacherService.getSupervisorClubReq(clubId).subscribe((res) => {
      console.log(res);
      this.clubReqArr = res;
      this.copyClubReqArr = Object.assign([], res);
      this.clubReqLoader = false;
    }, (err) => {
      console.log(err);
      this.clubReqLoader = false;
    });
  }

  filterRequests(val) {
    this.clubReqArr = this.filter(Object.assign([], this.copyClubReqArr), val, "Request");
  }

  newSessionBtn() {

  }

  // Actual Filtering of Sessions on the basis of type
  filter(array: any[], value: string, type: string) {
    let filterSessionArr = [];
    if (type == "Session") {
      if (value)
        filterSessionArr = array.filter(e => e.responses[0].status == value);
      else
        filterSessionArr = array;
    }
    if (type == "Student") {
      if (value)
        filterSessionArr = array.filter(e => e.gradeId == value);
      else
        filterSessionArr = array;
    }
    if (type == "Request") {
      if (value)
        filterSessionArr = array.filter(e => e.membershipStatus == value);
      else
        filterSessionArr = array;
    }

    return filterSessionArr;
  }

  // Adding Attachment at the time of create session
  onFileSelect(event) {
    this.name = ""; //reset the file name
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      console.log("File Uploaded", event.target.files[0]);
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

