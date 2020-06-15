import { Component, OnInit, Input } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-club-details',
  templateUrl: './club-details.component.html',
  styleUrls: ['./club-details.component.scss']
})
export class ClubDetailsComponent implements OnInit {

  // adminInfo: any;
  @Input() clubObj: any;
  // clubSupervisor = [];
  // sup_loader: boolean = false;

  constructor(private schoolService: SchoolService) { }

  ngOnInit() {
    // this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    // this.getSupervisor();
  }

  // ngOnChanges(clubObj: any) {
  //   this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
  //   this.getSupervisor();
  // }

  // getSupervisor() {
  //   this.clubSupervisor = [];
  //   this.sup_loader = true;
  //   this.schoolService.getClubSupervisor(this.adminInfo.schoolId, this.clubObj.id).subscribe((res) => {
  //     this.sup_loader = false;
  //     this.clubSupervisor = res;
  //     console.log(res);
  //   }, (err) => {
  //     console.log(err);
  //     this.sup_loader = false;
  //   });
  // }

}
