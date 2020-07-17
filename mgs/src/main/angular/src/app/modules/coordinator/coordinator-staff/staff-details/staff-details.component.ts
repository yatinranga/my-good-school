import { Component, OnInit, Input } from '@angular/core';
import { BASE_URL } from 'src/app/services/app.constant';

@Component({
  selector: 'app-staff-details',
  templateUrl: './staff-details.component.html',
  styleUrls: ['./staff-details.component.scss']
})
export class StaffDetailsComponent implements OnInit {

  BASE_URL: string;

  @Input() staffDetails: any;
  showClub: boolean = false;
  assignedClubsArr = [];
  assignedSocietyArr = [];

  constructor() {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
   }

  ngOnInit() {
  }

  ngOnChanges(staffDetails: any) {
    this.showClub = false;
  }

  /** Show List of Assigned Clubs/Socities */
  setShowClub(val: boolean) {
    this.showClub = val;
    if (val) {
      this.sortClubs();
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

}
