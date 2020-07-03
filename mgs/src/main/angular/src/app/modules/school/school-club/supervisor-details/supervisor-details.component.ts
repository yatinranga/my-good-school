import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { AlertService } from 'src/app/services/alert.service';
declare let $: any;


@Component({
  selector: 'app-supervisor-details',
  templateUrl: './supervisor-details.component.html',
  styleUrls: ['./supervisor-details.component.scss']
})
export class SupervisorDetailsComponent implements OnInit {

  adminInfo: any;
  @Input() clubObj: any;
  @Output() updatedClub = new EventEmitter<string>()

  sup_loader: boolean = false;
  clubSupervisor = [];
  schoolGrades = [];
  supervisorArr = [];
  gradesIds: any = {};
  supervisorId = "";
  supervisorName = "";
  supervisorClubArr = [];

  constructor(private schoolService: SchoolService, private alertService: AlertService) { }

  ngOnInit() {
    this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getSchoolGrades();
    this.getSchoolStaff();
  }

  ngOnChanges(clubObj: any) {
    this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getSupervisor();
  }

  /** Get List of Supervisor for the Selected Club/Society */
  getSupervisor() {
    this.clubSupervisor = [];
    this.sup_loader = true;
    this.schoolService.getClubSupervisor(this.adminInfo.schoolId, this.clubObj.id).subscribe((res) => {
      this.sup_loader = false;
      this.clubSupervisor = res;
      console.log(res);
    }, (err) => {
      this.sup_loader = false;
    });
  }

  /** Get List of All Grades of School */
  getSchoolGrades() {
    this.schoolService.getAllGrades(this.adminInfo.schoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
      (err) => console.log(err));
  }

  /** List of All Supervisor of School */
  getSchoolStaff() {
    this.schoolService.getStaff().subscribe((res) => {
      this.supervisorArr = res.filter(e => e.roles.includes("Supervisor"));
    })
  }

  /** Show Club/Society Assign Modal */
  showAssignModal() {
    $('#assignSupervisorModal').modal('show');

  }

  /** Assign Club/Society to Teacher */
  assignClub() {
    const arr = Object.values(this.gradesIds)
    if (arr.includes(true)) {
      const clubid = this.clubObj.id;

      // Response Body
      const reqBody = {
        teachers: [{
          id: this.supervisorId,
          activities: [{
            id: this.clubObj.id,
            grades: []
          }]
        }]
      };
      Object.keys(this.gradesIds).forEach((key) => {
        reqBody.teachers[0].activities[0].grades.push(key);
      });

      if (this.supervisorClubArr) {
        this.supervisorClubArr.forEach(ele => {
          const clubId = ele.id;
          const grades = [];
          ele.gradeResponses.forEach(element => {
            grades.push(element.id);
          });
          reqBody.teachers[0].activities.push({ id: clubId, grades: grades });
        })
      }

      this.clubSupervisor.forEach(e => {
        const supId = e.id;
        const activities = []
        e.activityAndGrades.forEach(ele => {
          const clubid = ele.id
          const grades = [];
          ele.gradeResponses.forEach(element => {
            grades.push(element.id);
          });
          activities.push({ id: clubid, grades: grades });
        });
        reqBody.teachers.push({ id: supId, activities: activities });
      });

      console.log(reqBody);

      this.alertService.showLoader("");
      this.schoolService.assignClub(reqBody).subscribe((res) => {
        this.clubSupervisor = res.teachers;
        this.updatedClub.emit("Club-Supervisor Updated");
        $('#assignSupervisorModal').modal('hide');
        this.resetForm();
        this.alertService.showMessageWithSym("Supervisor Assigned !", "Success", "success");
      }, (err) => {
        this.errorMessage(err);
      })
    }
    else {
      this.alertService.showMessageWithSym("Please select atleast one grade", "", "info");
    }
  }

  /** Show Edit Grades Modal */
  editGradesButton(sup_obj) {
    this.supervisorName = sup_obj.name;
    this.supervisorId = sup_obj.id;
    $('#editGradesModal').modal('show');
    sup_obj.activityAndGrades.forEach(element => {
      if (element.name == this.clubObj.name) {
        element.gradeResponses.forEach(element => {
          this.gradesIds[element.id] = true;
        });
      }
    });
  }

  /** Update/Edit Grades */
  editGrades() {
    const arr = Object.values(this.gradesIds)
    if (arr.includes(true)) {
      console.log(this.gradesIds);
      // Response Body
      const reqBody = {
        teachers: []
      };

      this.clubSupervisor.forEach(e => {
        if (e.id == this.supervisorId) {
          const supId = e.id;
          const activities = []
          e.activityAndGrades.forEach(ele => {
            const clubid = ele.id
            const grades = [];
            if (ele.id == this.clubObj.id) {
              Object.keys(this.gradesIds).forEach((key) => {
                if (this.gradesIds[key])
                  grades.push(key);
              });
            }
            else {
              ele.gradeResponses.forEach(element => {
                grades.push(element.id);
              });
            }
            activities.push({ id: clubid, grades: grades });
          });
          reqBody.teachers.push({ id: supId, activities: activities });
        }
        else {
          const supId = e.id;
          const activities = []
          e.activityAndGrades.forEach(ele => {
            const clubid = ele.id
            const grades = [];
            ele.gradeResponses.forEach(element => {
              grades.push(element.id);
            });
            activities.push({ id: clubid, grades: grades });
          });
          reqBody.teachers.push({ id: supId, activities: activities });
        }
      });
      console.log(reqBody);
      this.alertService.showLoader("");
      this.schoolService.assignClub(reqBody).subscribe((res) => {
        this.clubSupervisor = res.teachers;
        this.updatedClub.emit("Club-Supervisor Updated");
        $('#editGradesModal').modal('hide');
        this.resetForm();
        this.alertService.showMessageWithSym("Grades Edited !", "Success", "success");
      }, (err) => {
        this.errorMessage(err);
      })
    }
    else {
      this.alertService.showMessageWithSym("Please select atleast one grade", "", "info");
    }
  }

  /** Unassign Club/Society for a particular Supervisor */
  unassignClub(sup_obj) {
    // Response Body
    const reqBody = {
      teachers: []
    };

    this.alertService.confirmWithoutLoader('question', 'Are you sure you want to unassign ?', '', 'Yes').then(result => {
      if (result.value) {
        this.clubSupervisor.forEach(e => {
          const supId = e.id;
          const activities = []
          e.activityAndGrades.forEach(ele => {
            if ((supId == sup_obj.id) && (ele.id == this.clubObj.id)) { }
            else {
              const clubid = ele.id
              const grades = [];
              ele.gradeResponses.forEach(element => {
                grades.push(element.id);
              });
              activities.push({ id: clubid, grades: grades });
            }
          });
          reqBody.teachers.push({ id: supId, activities: activities });
        });
        console.log(reqBody);
        this.alertService.showLoader("");
        this.schoolService.assignClub(reqBody).subscribe((res) => {
          this.getSupervisor();
          this.updatedClub.emit("Club-Supervisor Updated");
          this.resetForm();
          this.alertService.showMessageWithSym("Club Unassigned !", "Success", "success");
        }, (err) => {
          console.log(err);
          this.errorMessage(err);
        });
      }
    });
  }

  /** Get All Clubs/Societies of a particular Supervisor (during Club assign) */
  getSupervisorClubs(teacherId) {
    this.supervisorClubArr = [];
    this.schoolService.getSupervisorClubs(teacherId, this.adminInfo.schoolId).subscribe(res => {
      console.log(res);
      this.supervisorClubArr = res;
    }, (err => {
      console.log(err);
    }))
  }

  /** Reset Form and Values */
  resetForm() {
    this.supervisorId = "";
    this.gradesIds = {};
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
  selectGrades(val) {
    if (val == "All") {
      this.schoolGrades.forEach(e => {
        this.gradesIds[e.id] = true;
      });
    }
    if (val == "Reset") {
      this.gradesIds = {};
    }
  }
}
