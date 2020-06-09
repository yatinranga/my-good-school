import { Component, OnInit } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-staff-list',
  templateUrl: './staff-list.component.html',
  styleUrls: ['./staff-list.component.scss']
})
export class StaffListComponent implements OnInit {

  col = "col-12";
  showDetails: boolean = false;
  staffArr: any = []
  staff_loader = false;

  constructor(private schoolService: SchoolService, private alertService: AlertService) { }

  ngOnInit() {
    this.getAllStaff();
  }

  /** Get List of All Staff Members  */
  getAllStaff() {
    this.staff_loader = true;
    this.schoolService.getStaff().subscribe((res) => {
      this.staffArr = res;
      console.log(res);
      this.staff_loader = false;
    }, (err) => {
      this.staff_loader = false;
      console.log(err);
      if (err.status == 400) {
        this.alertService.showMessageWithSym(err.msg, "", "info");
      }
      else {
        this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
      }
    })
  }

  /** Set Show Details */
  setShowDetails(val:boolean){
    this.showDetails = val;
    this.showDetails ? (this.col = "col-6") : (this.col="col-12");
  }

}
