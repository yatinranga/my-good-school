import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
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
  @Output() rowChangeForClub = new EventEmitter<string>(); // When Clubs window is open
  showClub: boolean = false;
  assignedClubsArr = [];
  assignedSocietyArr = [];
  allSchoolClubsArr = [];
  schoolGrades = [];
  clubName = ""; // used to show Club name during assign grade in assignGradeModal
  imagePath = "assets/images/childprofile.jpg";

  assignClubForm: FormGroup;
  assignGradeForm: FormGroup;
  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {

    this.assignClubForm = this.formBuilder.group({
      id: [this.staffDetails.id],
      activitiesIds: [, [Validators.required]]
    });

    this.assignGradeForm = this.formBuilder.group({
      gradeIds:[,[Validators.required]]
    })

    this.getSchoolClubs();
    this.getSchoolGrades();
  }

  ngOnChanges(staffDetails: any) {
    this.showClub = false;
  }

  /** Show List of Assigned Clubs/Socities */
  setShowClub(val: boolean) {
    this.showClub = val;
    this.showClub ? (this.col = "col-6") : (this.col = "col-12");

    if (val) {
      const col = "col-4";
      this.rowChangeForClub.emit(col);
      // this.getEnrolledClubs();
      this.sortClubs();
    }
    else {
      const col = "col-6";
      this.rowChangeForClub.emit(col);
    }
  }

  /** Sorting Clubs and Society in separate array */
  sortClubs() {
    this.assignedClubsArr = [];
    this.assignedSocietyArr = [];
    if (this.staffDetails['activityAndGrades']) {
      this.assignedClubsArr = this.staffDetails['activityAndGrades'].filter((e) => (e.clubOrSociety == 'Club'));
      this.assignedSocietyArr = this.staffDetails['activityAndGrades'].filter((e) => (e.clubOrSociety == 'Society'));

    }
  }

  /** Show assign Club/Society Modal */
  showAssignModal() {
    const arr = [];
    $('#assignClubModal').modal('show');
    if (this.staffDetails['activityAndGrades']) {
      this.staffDetails['activityAndGrades'].forEach((e) => {
        arr.push(e.id);
        this.assignClubForm.controls.activitiesIds.setValue(arr);
        // console.log(this.assignClubForm.value.activitiesIds);
      })
    }
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
  getSchoolGrades(){
    this.schoolService.getAllGrades(this.staffDetails.schoolId).subscribe((res) => {
      this.schoolGrades = res;
    },
    (err) => console.log(err));
  }

  /** Assign Club/Society to Teacher */
  assignClub() {
    this.assignClubForm.value.id = this.staffDetails.id;
    console.log(this.assignClubForm.value);
    this.alertService.showLoader("");
    this.schoolService.assignClub(this.assignClubForm.value).subscribe((res) => {
      console.log(res);
      this.alertService.showMessageWithSym("Club/Society Assigned !", "Success", "success");
    }, (err) => {
      if (err.status === 500) {
        this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
      } else {
        this.alertService.showMessageWithSym("", "Error", "error");
      }
    })
  }

  /** Show Edit Grades Modal */
  showGradeEditModal(club_obj){
    $('#assignGradeModal').modal('show');
    this.clubName = club_obj.name;
    const arr = [];
    club_obj.gradeResponses.forEach(element => {
      console.log("element.id - ",element.id);
      arr.push(element.id);
      this.assignGradeForm.controls.gradeIds.setValue(arr);
    });
  }

  /** Assign Grade for a CLub/Society to Supervisor/Coordinator*/
  assignGrade(){

  }

  /** Reset Form */
  resetForm() {
    this.assignClubForm.reset()
    this.assignGradeForm.reset();
  }

}
