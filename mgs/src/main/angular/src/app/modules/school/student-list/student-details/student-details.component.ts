import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';
declare let $: any;

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrls: ['./student-details.component.scss']
})
export class StudentDetailsComponent implements OnInit {

  col = "col-12";
  @Input() studentDetails: any;
  @Output() rowChangeForClub = new EventEmitter<string>() // When Enrolled Clubs are shown
  showClub: boolean;
  imagePath = "assets/images/childprofile.jpg";
  // showClub:boolean = false;
  studentEnrolledClubArr = [];
  studentEnrolledSociArr = [];
  guardianModalType: string = ""; // used to show title in add/edit guardian modal 
  club_loader = false;

  guardianForm: FormGroup;
  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder, private alertService: AlertService) {
    this.showClub = false
  }

  ngOnInit() {
    this.guardianForm = this.formBuilder.group({
      id: [null],
      name: [, [Validators.required]],
      email: [],
      gender: [, [Validators.required]],
      mobileNumber: [],
      relationship: [, [Validators.required]],
      studentIds: [[this.studentDetails.id]]
    })
  }

  ngOnChanges(studentDetails: any) {
    this.showClub = false;
  }

  /** To show the Club/Society List */
  setShowClub(val: boolean) {
    this.showClub = val;
    this.showClub? (this.col="col-12"):(this.col="col-12");
    
    if(val){
      // const col="col-4"
      // this.rowChangeForClub.emit(col);
      this.getEnrolledClubs();
    }
    else{
      // const col="col-6"
      // this.rowChangeForClub.emit(col);
    }
  }

  /** Get List of Enrolled clubs and Socities */
  getEnrolledClubs() {
    this.club_loader = true;
    this.studentEnrolledClubArr = [];
    this.studentEnrolledSociArr = [];
    this.schoolService.getStudentClubs(this.studentDetails.id).subscribe((res) => {
      this.studentEnrolledClubArr = res.filter((e) => (e.clubOrSociety == 'Club'));
      this.studentEnrolledSociArr = res.filter((e) => (e.clubOrSociety == 'Society'));
      this.club_loader = false;
    }, (err) => {
      console.log(err);
      this.club_loader = false;
    });

  }

  /** Show guardian Add/Edit Modal  */
  showGuardianModal(val: string, guardianObj?) {
    if (val == "Add") {
      this.guardianModalType = val;
      $('#editGuardianModal').modal('show');
    }

    if (val == "Edit") {
      this.guardianModalType = val;
      $('#editGuardianModal').modal('show');
      console.log(guardianObj);

      this.guardianForm.patchValue({
        id: guardianObj.id,
        name: guardianObj.name,
        mobileNumber: guardianObj.mobileNumber,
        gender: guardianObj.gender,
        email: guardianObj.email,
        relationship: guardianObj.relationship
      });
    }
  }

  /** Submit the Guardian */
  submitGuardian() {
    console.log(this.guardianForm.value);
    this.alertService.showLoader("");
    this.schoolService.editGuardian(this.guardianForm.value).subscribe((res) => {
      console.log(res);
      this.alertService.showMessageWithSym("Guardian Added !", "Successful", "success");
    }, (err) => {
      this.errorMessage(err);
    })
  }

  /** Reset Form */
  resetForm() {
    this.guardianForm.reset();
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
}
