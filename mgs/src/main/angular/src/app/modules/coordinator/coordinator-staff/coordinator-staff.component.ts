import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/services/teacher.service';

@Component({
  selector: 'app-coordinator-staff',
  templateUrl: './coordinator-staff.component.html',
  styleUrls: ['./coordinator-staff.component.scss']
})
export class CoordinatorStaffComponent implements OnInit {

  coordinatorInfo: any;
  supervisorArr = [];
  copySupervisorArr = [];
  sup_loader: boolean = false;
  search:any // used for search
  clubName="" //used for Filter by clubs and
  allClubsArr = [];
  constructor(private teacherService: TeacherService) { }

  ngOnInit() {
    this.coordinatorInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getSupervisors();
    this.getAllClubs();
  }

  /** Get List of Supervisor Under Coordinator */
  getSupervisors() {
    this.sup_loader = true;
    this.teacherService.getSupervisor(this.coordinatorInfo.schoolId).subscribe(res => {
      console.log(res);
      this.supervisorArr = res;
      this.copySupervisorArr = Object.assign([],res);
      this.sup_loader = false;
    }, (err => {
      console.log(err);
      this.sup_loader = false;
    }))
  }

  /** Get List of All Clubs in School */
  getAllClubs(){
    this.teacherService.getActivity(this.coordinatorInfo.schoolId).subscribe(res=>{
      this.allClubsArr = res;
    },(err=>{console.log(err)}))
  }

  /** Filter Staff on the basis of Clubs */
  filterStaff(){
    this.supervisorArr = this.filter(Object.assign([],this.copySupervisorArr));
  }

  /** Actual filter on the basis of Club */
  filter(array:any[]){
    let filterArr = []
    if(this.clubName){
      filterArr = array.filter(e => e.activities && e.activities.includes(this.clubName));
    } else {
      filterArr = array;
    }
    return filterArr;
  }

}
