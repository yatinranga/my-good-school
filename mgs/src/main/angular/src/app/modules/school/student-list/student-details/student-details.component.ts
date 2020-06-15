import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrls: ['./student-details.component.scss']
})
export class StudentDetailsComponent implements OnInit {

  col = "col-12";
  @Input() studentDetails: any;
  @Output() rowChangeForClub = new EventEmitter<string>() // When Enrolled Clubs are shown
  showClub:boolean;
  imagePath  = "assets/images/childprofile.jpg";
  // showClub:boolean = false;
  studentEnrolledClubArr = [];
  studentEnrolledSociArr = [];
  constructor(private schoolService: SchoolService) {
    this.showClub = false
  }

  ngOnInit() {
  }

  ngOnChanges(studentDetails:any){
    this.showClub = false;
  }

  setShowClub(val:boolean){
    this.showClub = val;
    this.showClub? (this.col="col-6"):(this.col="col-12");
    
    if(val){
      const col="col-4"
      this.rowChangeForClub.emit(col);
      this.getEnrolledClubs();
    }
    else{
      const col="col-6"
      this.rowChangeForClub.emit(col);
    }
  }

  getEnrolledClubs(){
    this.studentEnrolledClubArr = [];
    this.studentEnrolledSociArr = [];    
    this.schoolService.getStudentClubs(this.studentDetails.id).subscribe((res) => {
      this.studentEnrolledClubArr = res.filter((e) => (e.clubOrSociety=='Club'));
      this.studentEnrolledSociArr = res.filter((e) => (e.clubOrSociety=='Society'));
    },(err) => {
      console.log(err);
    })

  }
}
