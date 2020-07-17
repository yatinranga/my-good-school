import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';
declare let $: any;


@Component({
  selector: 'app-staff-details',
  templateUrl: './staff-details.component.html',
  styleUrls: ['./staff-details.component.scss']
})
export class StaffDetailsComponent implements OnInit {

  col = "col-12";
  @Input() staffDetails: any;
  @Output() profileUpdated = new EventEmitter<string>(); // When Clubs window is open
  showClub: boolean = false;
  assignedClubsArr = [];
  assignedSocietyArr = [];
  allAssignedActivity = [];
  allSchoolClubsArr = [];
  schoolGrades = [];
  clubName = ""; // used to show Club name during assign grade in assignGradeModal
  imagePath = "assets/images/teacherprofile1.jpg";

  clubIds = {}; // used to assign Club to Supervisor through modal
  gradesIds = {} // used to assign Grades to Supervisor through modal
  clubId = ""; // used to assign single club/society
  rolesArr = [];
  roleName: any = "";
  isRoleChanged:boolean = false;

  updateSupervisorForm: FormGroup;

  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.getSchoolClubs();
    this.getSchoolGrades();
    this.updateSupervisorForm = this.formBuilder.group({
      id: [],
      name: [],
      email: [],
      dob: [, [Validators.required]],
      profileBrief: [],
      mobileNumber: [],
      gender: [],
      qualification: [],
      yearOfEnrolment: [],
      gradeIds : [null]
    });
    this.getRoles();
  }

  ngOnChanges(staffDetails: any) {
    this.showClub = false;
    this.clubIds = {};
  }

  /** get all the Roles */
  getRoles() {
    this.schoolService.getRoles().subscribe((res) => {
      this.rolesArr = res;
    }, (err) => { console.log(err); })
  }

  /** Show List of Assigned Clubs/Socities */
  setShowClub(val: boolean) {
    this.showClub = val;
    this.showClub ? (this.col = "col-12") : (this.col = "col-12");

    if (val) {
      // const col = "col-4";
      // this.getEnrolledClubs();
      this.sortClubs();
    }
    else {
      // const col = "col-6";
      // this.rowChangeForClub.emit(col);
    }
  }

  /** Sorting Clubs and Society in separate array */
  sortClubs() {
    this.assignedClubsArr = [];
    this.assignedSocietyArr = [];
    if (this.staffDetails['activityAndGrades']) {
      this.allAssignedActivity = this.staffDetails['activityAndGrades'];
      this.assignedClubsArr = this.staffDetails['activityAndGrades'].filter((e) => (e.clubOrSociety == 'Club'));
      this.assignedSocietyArr = this.staffDetails['activityAndGrades'].filter((e) => (e.clubOrSociety == 'Society'));
    }
  }

  /** Show assign Club/Society Modal */
  showAssignModal() {
    $('#assignClubModal').modal('show');
    this.clubId = "";
    this.gradesIds = {};
  }

  /** Get List of All Clubs/Socities of School */
  getSchoolClubs() {
    this.schoolService.getAllClubs(this.staffDetails.schoolId).subscribe((res) => {
      this.allSchoolClubsArr = res;
    }, (err) => {
      console.log(err);
    })
  }

  /** Get List of All Grades of School */
  getSchoolGrades() {
    this.schoolService.getAllGrades(this.staffDetails.schoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
      (err) => console.log(err));
  }

  /** Assign Club/Society to Teacher */
  assignClub() {
    // Response Body
    const reqBody = {
      teachers: [{
        id: this.staffDetails.id,
        activities: []
      }]
    };

    // Response Format to assign new Club/Society
    const clubId = {
      id: this.clubId,
      grades: []
    }
    Object.keys(this.gradesIds).forEach((key) => {
      if (this.gradesIds[key]) {
        clubId.grades.push(key);
      }
    });
    reqBody.teachers[0].activities.push(clubId);


    // To add previous assigned Club/Society in Response
    if (this.allAssignedActivity) {
      this.allAssignedActivity.forEach(e => {
        const cid = e.id;
        const gradesArr = [];

        e.gradeResponses.forEach(ele => {
          gradesArr.push(ele.id);
        });

        const res = {
          id: cid,
          grades: gradesArr
        }
        reqBody.teachers[0].activities.push(res);
      })
    }

    console.log(reqBody);
    this.alertService.showLoader("");
    this.schoolService.assignClub(reqBody).subscribe((res) => {
      this.staffDetails = res.teachers[0];
      this.sortClubs(); // Reload to see the New Assigned Club/Society
      this.clubIds = {};
      $('#assignClubModal').modal('hide');
      this.alertService.showMessageWithSym("Club/Society Assigned !", "Success", "success");
    }, (err) => {
      console.log(err);
      this.errorMessage(err);
    })
  }

  /** Show Edit Grades Modal */
  showGradeEditModal(club_obj) {
    console.log(club_obj);
    this.gradesIds = {};
    $('#assignGradeModal').modal('show');
    this.clubName = club_obj.name;
    this.clubId = club_obj.id;
    club_obj.gradeResponses.forEach(element => {
      this.gradesIds[element.id] = true;
    });
  }

  /** Assign Grade for a CLub/Society to Supervisor/Coordinator*/
  assignGrade() {
    // Response Body
    const reqBody = {
      teachers: [{
        id: this.staffDetails.id,
        activities: []
      }]
    };

    // Response Format to assign edit grades
    const clubId = {
      id: this.clubId,
      grades: []
    }
    Object.keys(this.gradesIds).forEach((key) => {
      if (this.gradesIds[key]) {
        clubId.grades.push(key);
      }
    });
    reqBody.teachers[0].activities.push(clubId);


    // To add previous assigned Club/Society in Response
    if (this.allAssignedActivity) {
      this.allAssignedActivity.forEach(e => {
        if (!(this.clubId == e.id)) {
          const cid = e.id;
          const gradesArr = [];

          e.gradeResponses.forEach(ele => {
            gradesArr.push(ele.id);
          });

          const res = {
            id: cid,
            grades: gradesArr
          }
          reqBody.teachers[0].activities.push(res);
        }
      })
    }

    console.log(reqBody);
    this.alertService.showLoader("");
    this.schoolService.assignClub(reqBody).subscribe((res) => {
      this.profileUpdated.emit("Grade Assigned");
      this.staffDetails = res.teachers[0];
      this.sortClubs(); // Reload to see the New Assigned Club/Society
      this.clubIds = {};
      $('#assignGradeModal').modal('hide');
      this.alertService.showMessageWithSym("Grade Assigned !", "Success", "success");
    }, (err) => {
      console.log(err);
      this.errorMessage(err);
    })

  }

  /** Show Supervisor Edit/Update Profile Modal */
  showSupervisorModal() {
    $('#editSupervisorModal').modal('show');
    if (this.staffDetails.dob)
      this.updateSupervisorForm.controls.dob.patchValue(this.staffDetails.dob.split(' ')[0]);
    this.roleName = this.staffDetails.roles[0];

    this.updateSupervisorForm.patchValue({
      id: this.staffDetails.id,
      name: this.staffDetails.name,
      profileBrief: this.staffDetails.profileBrief,
      email: this.staffDetails.email,
      mobileNumber: this.staffDetails.mobileNumber,
      gender: this.staffDetails.gender,
      qualification: this.staffDetails.qualification,
      yearOfEnrolment: this.staffDetails.yearOfEnrolment,
    })

    if(this.staffDetails.grades){
      this.staffDetails.grades.forEach(element => {
        this.gradesIds[element.id] = true;
      });
    }
  }

  /** Update Supervisor Profile */
  updateSupervisorProfile() {
    //to Update the Role
    if(this.isRoleChanged){ 
      this.rolesArr.forEach(e=>{
        if(e.name==this.roleName){
          const reqBody = { roleIds : [e.id]};
          this.schoolService.updateRole(this.staffDetails.userId,reqBody).subscribe(res=>{
            this.profileUpdated.emit("Profile Updated");
          })
        }
      })
    }

    const gradeIds = []
    Object.keys(this.gradesIds).forEach((key) => {
      if (this.gradesIds[key]) {
        gradeIds.push(key);
      }
    });

    this.updateSupervisorForm.value.gradeIds = gradeIds;
    console.log(this.updateSupervisorForm.value);
    this.updateSupervisorForm.value.dob = this.updateSupervisorForm.value.dob + " 00:00:00";
    this.alertService.showLoader("");
    this.schoolService.updateSupervisorProfile(this.staffDetails.id, this.updateSupervisorForm.value).subscribe((res) => {
      this.profileUpdated.emit("Profile Updated");
      this.staffDetails = res;
      $('#editSupervisorModal').modal('hide');
      this.alertService.showMessageWithSym("Profile Updated !", "Success", "success");
    }, (err) => {
      console.log(err);
      this.errorMessage(err);
    });
  }

  /** Unassign Club/Society */
  unassignClub(club_obj) {
    // Response Body
    const reqBody = {
      teachers: [{
        id: this.staffDetails.id,
        activities: []
      }]
    };

    this.alertService.confirmWithoutLoader('question', 'Are you sure you want to unassign '+club_obj.name+' ?', '', 'Yes').then(result => {
      if (result.value) {
        this.staffDetails['activityAndGrades'].forEach(ele => {
          if (ele.id == club_obj.id) { }
          else {
            const clubid = ele.id
            const grades = [];
            ele.gradeResponses.forEach(element => {
              grades.push(element.id);
            });
            reqBody.teachers[0].activities.push({ id: clubid, grades: grades });
          }
        });
        console.log(reqBody);
        this.alertService.showLoader("");
        this.schoolService.assignClub(reqBody).subscribe((res) => {
          console.log("Res of unassing - ",res)
          this.staffDetails = res.teachers[0];
      this.sortClubs(); // Reload to see the New Assigned Club/Society
          this.profileUpdated.emit("Club-Supervisor Updated");
          this.alertService.showMessageWithSym(club_obj.clubOrSociety + " Unassigned !", "Success", "success");
        }, (err) => {
          console.log(err);
          this.errorMessage(err);
        });
      }
    });
  }

  updateRole(event) {
    console.log(event);
    this.isRoleChanged = true;
  }

  /** Selected Club Ids */
  // selectedClubIds(){
  //   console.log(this.clubIds);
  // }

  /** Reset Form */
  resetForm() {
    this.clubIds = {};
    this.gradesIds = {};
    this.updateSupervisorForm.reset();
    this.clubId = "";
    this.roleName = "";
  }

  /** Handling Error */
  errorMessage(err) {
    if (err.status == 400) {
      this.alertService.showMessageWithSym(err.msg, "", "info");
    }
    else {
      this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
    }
  }

  /** Select and Deselect all grades */
  selectGrades(val){
    if(val=="All"){
      this.schoolGrades.forEach(e=>{
        this.gradesIds[e.id] = true;
      });
    }
    if(val=="Reset"){
      this.gradesIds = {};
    }
  }

}
