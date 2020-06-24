import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-staff-details',
  templateUrl: './staff-details.component.html',
  styleUrls: ['./staff-details.component.scss']
})
export class StaffDetailsComponent implements OnInit {

  @Input() staffDetails: any;
  imagePath = "assets/images/teacherprofile1.jpg";
  showClub: boolean = false;
  assignedClubsArr = [];
  assignedSocietyArr = [];

  constructor() { }

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
