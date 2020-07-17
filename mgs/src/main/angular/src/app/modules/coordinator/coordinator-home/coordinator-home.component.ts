import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';

@Component({
  selector: 'app-coordinator-home',
  templateUrl: './coordinator-home.component.html',
  styleUrls: ['./coordinator-home.component.scss']
})
export class CoordinatorHomeComponent implements OnInit {

  userInfo: any;
  allActivities: any;
  sportArr = [];
  serviceArr = [];
  studyArr = [];
  skillArr = [];
  clubLoader: boolean = false;
  showClubDetails: boolean = false;
  club_Object:any;

  constructor(private teacherService: TeacherService) { }

  ngOnInit() {
    this.userInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getClubsInGrades();
  }

  /** Get Clubs/Socities running in Coordinator grades */
  getClubsInGrades() {
    this.clubLoader = true;
    this.teacherService.getUserClubsInGrades(this.userInfo.schoolId).subscribe(res => {
      this.clubLoader = false;
      this.allActivities = res;
      this.sportArr = res.filter((e) => (e.fourS == 'Sport'));
      this.serviceArr = res.filter((e) => (e.fourS == 'Service'));
      this.studyArr = res.filter((e) => (e.fourS == 'Study'));
      this.skillArr = res.filter((e) => (e.fourS == 'Skill'));
    }, (err => {
      console.log(err);
      this.clubLoader = false;
    }))
  }

  /** Details of All Clubs and Societies */
  setClubDetails(val: boolean, clubObj?) {
    this.showClubDetails = val
    this.club_Object = clubObj;
  }

}
