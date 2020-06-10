import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';

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
  imagePath = "assets/images/childprofile.jpg";

  constructor(private schoolService: SchoolService) { }

  ngOnInit() {
  }

  ngOnChanges(staffDetails: any) {
    this.showClub = false;
  }

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

  sortClubs() {
    this.assignedClubsArr = [];
    this.assignedSocietyArr = [];
    if (this.staffDetails['activityAndGrades']) {
      this.assignedClubsArr = this.staffDetails['activityAndGrades'].filter((e) => (e.clubOrSociety == 'Club'));
      this.assignedSocietyArr = this.staffDetails['activityAndGrades'].filter((e) => (e.clubOrSociety == 'Society'));

    }
  }

}
