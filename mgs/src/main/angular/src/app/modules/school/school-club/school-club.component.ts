import { Component, OnInit } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';

declare let $: any;

@Component({
  selector: 'app-school-club',
  templateUrl: './school-club.component.html',
  styleUrls: ['./school-club.component.scss']
})
export class SchoolClubComponent implements OnInit {

  col = "col-12";
  adminInfo: any;
  schoolClubs = [];
  copySchoolClubs = [];
  showSupervisor: boolean = false
  showClubDetails: boolean = false
  club_loader: boolean = false;
  club_obj: any; // Used to send Club Obj to Club-details Component
  search: any = ""; //Used for Search in table

  psdAreaArr = [];
  focusAreaArr = [];
  fourSArr = [];
  fourS: any = '';
  psdAreas: any = '';
  focusAreas: any = '';
  clubType: any = '';
  clubId: any;

  addClubForm: FormGroup;
  focusareaIds = {};

  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getClubs();
    this.getFilters();
    this.getFocusArea();

    this.addClubForm = this.formBuilder.group({
      name: [, [Validators.required]],
      description: [],
      schoolIds: [[this.adminInfo.schoolId]],
      fourS: [, [Validators.required]],
      clubOrSociety: [, [Validators.required]],
      focusAreaRequests: []
    })
  }

  /** List of All Clubs and Socities offered in School */
  getClubs() {
    this.club_loader = true;
    this.schoolService.getAllClubs(this.adminInfo.schoolId).subscribe((res) => {
      this.club_loader = false;
      this.schoolClubs = res;
      this.copySchoolClubs = Object.assign([], res);
    }, (err) => {
      console.log(err);
      this.club_loader = false;
    })
  }

  getFocusArea() {
    this.schoolService.getFocusArea().subscribe(res => {
      this.focusAreaArr = res;
    }, (err => {
      console.log(err);
    }))
  }

  /** List of All Filters -  */
  getFilters() {
    this.schoolService.getActivityAreas().subscribe((res) => {
      this.psdAreaArr = res["PSD Areas"]
      // this.focusAreaArr = res["Focus Areas"]
      this.fourSArr = res["Four S"]
    }, (err) => { console.log(err); })
  }

  /** Filter Clubs/Societies on th basis of PSD, 4S and Focus Area */
  filterClubs() {
    this.schoolClubs = this.filter(Object.assign([], this.copySchoolClubs));
  }

  /** Actual Filterting of Awards on the basis of selected criteria */
  filter(array: any[]) {
    let filterClubsArr = [];
    if (this.clubType && this.fourS) {
      filterClubsArr = array.filter(e => e.fourS && e.fourS == this.fourS && e.clubOrSociety && e.clubOrSociety == this.clubType);
    }
    else if (this.clubType) {
      filterClubsArr = array.filter(e => e.clubOrSociety && e.clubOrSociety == this.clubType);
    }
    else if (this.fourS) {
      filterClubsArr = array.filter(e => e.fourS && e.fourS == this.fourS);
    }
    else {
      filterClubsArr = array;
    }
    return filterClubsArr;
  }

  setShowWindow(type, club_obj?) {
    console.log(club_obj);

    if (type == 'supervisor') {
      this.showClubDetails = false;
      this.showSupervisor = true;
      this.showSupervisor ? (this.col = "col-8") : (this.col = "col-12");
      this.club_obj = club_obj;
      this.clubId = club_obj.id;
    }

    if (type == 'club') {
      this.showClubDetails = true;
      this.showClubDetails ? (this.col = "col-8") : (this.col = "col-12");
      this.showSupervisor = false;
      this.club_obj = club_obj;
      this.clubId = club_obj.id;
    }

    if (type == 'No Window') {
      this.showClubDetails = false;
      this.showSupervisor = false;
      this.col = "col-12";
    }
  }

  addClub() {
    const arr = [];
    Object.keys(this.focusareaIds).forEach(key => {
      if (this.focusareaIds[key]) {
        arr.push({ id: key })
      }
    })
    this.addClubForm.value.focusAreaRequests = arr;

    if (Object.keys(this.focusareaIds).length) {
      this.alertService.showLoader("");

      console.log(this.addClubForm.value);
      this.schoolService.updateClub(this.addClubForm.value).subscribe(res => {
        console.log(res);
        $('#addClubModal').modal('hide');
        this.resetForm();
        this.schoolClubs.unshift(res);
        this.alertService.showMessageWithSym('Club Added !', 'Updated', 'success');
      }, (err => {
        console.log(err);
        this.errorMessage(err);
      }))
    } else {
      this.alertService.showMessageWithSym('Fill all the fields !', "", "warning");
    }
  }

  /** Reload the Table when Club/Society Details are updated*/
  UpdatedTable(event){
    this.getClubs();
  }

  resetForm() {
    this.addClubForm.reset();
    this.focusareaIds = {};
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
