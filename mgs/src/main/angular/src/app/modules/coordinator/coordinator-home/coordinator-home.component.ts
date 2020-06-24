import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';

@Component({
  selector: 'app-coordinator-home',
  templateUrl: './coordinator-home.component.html',
  styleUrls: ['./coordinator-home.component.scss']
})
export class CoordinatorHomeComponent implements OnInit {

  userInfo:any;
  constructor(private teacherService: TeacherService) { }

  ngOnInit() {
    this.userInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getClubsInGrades();
  }

  getClubsInGrades(){
    this.teacherService.getUserClubsInGrades(this.userInfo.schoolId).subscribe(res=>{
      console.log(res);
    },(err=>{
      console.log(err);
    }))
  }

}
