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
  sup_loader: boolean = false;
  constructor(private teacherService: TeacherService) { }

  ngOnInit() {
    this.coordinatorInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getSupervisors();
  }

  getSupervisors() {
    this.sup_loader = true;
    this.teacherService.getSupervisor(this.coordinatorInfo.schoolId).subscribe(res => {
      console.log(res);
      this.supervisorArr = res;
      this.sup_loader = false;
    }, (err => {
      console.log(err);
      this.sup_loader = false;
    }))
  }

}
