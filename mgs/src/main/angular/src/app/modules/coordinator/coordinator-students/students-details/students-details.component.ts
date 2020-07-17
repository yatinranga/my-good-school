import { Component, OnInit, Input } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';
import { BASE_URL } from 'src/app/services/app.constant';

@Component({
  selector: 'app-students-details',
  templateUrl: './students-details.component.html',
  styleUrls: ['./students-details.component.scss']
})
export class StudentsDetailsComponent implements OnInit {

  BASE_URL: string;

  @Input() studentDetails: any
  // imagePath = "assets/images/childprofile.jpg";
  showClub: boolean = false;
  studentEnrolledClubArr = [];
  studentEnrolledSociArr = [];
  club_loader: boolean = false;

  constructor(private teacherService: TeacherService) {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
   }

  ngOnInit() {
  }
  ngOnChanges(studentDetails: any) {
    this.showClub = false;
  }

  /** To show the Club/Society List */
  setShowClub(val: boolean) {
    this.showClub = val;
    if (val) {
      this.getEnrolledClubs();
    }
  }

  /** Get List of Enrolled clubs and Socities */
  getEnrolledClubs() {
    this.club_loader = true;
    this.studentEnrolledClubArr = [];
    this.studentEnrolledSociArr = [];
    this.teacherService.getStudentClubs(this.studentDetails.id).subscribe((res) => {
      this.studentEnrolledClubArr = res.filter((e) => (e.clubOrSociety == 'Club'));
      this.studentEnrolledSociArr = res.filter((e) => (e.clubOrSociety == 'Society'));
      this.club_loader = false;
    }, (err) => {
      console.log(err);
      this.club_loader = false;
    });

  }

}
