import { Component, OnInit } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-school-club',
  templateUrl: './school-club.component.html',
  styleUrls: ['./school-club.component.scss']
})
export class SchoolClubComponent implements OnInit {
  
  col = "col-12";
  adminInfo: any;
  schoolClubs = [];
  showSupervisor:boolean = false
  club_loader:boolean = false;
  club_obj: any; // Used to send Club Obj to Club-details Component

  constructor(private schoolService: SchoolService) { }

  ngOnInit() {
    this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getClubs();
  }

  getClubs(){
    this.club_loader = true;
    this.schoolService.getAllClubs(this.adminInfo.schoolId).subscribe((res) => {
    this.club_loader = false;
      console.log(res);
      this.schoolClubs = res;
    },(err) => {
      console.log(err);
    this.club_loader = false;
    })
  }

  setShowSupervisor(val: boolean, club_obj?){
    console.log(club_obj);
    this.club_obj = club_obj;
    this.showSupervisor = val;
    this.showSupervisor? (this.col="col-6") : (this.col = "col-12");
  }

}
