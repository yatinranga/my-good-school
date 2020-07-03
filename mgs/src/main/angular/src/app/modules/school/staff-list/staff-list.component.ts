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
  copyStaffArr: any = [];
  staff_loader = false;
  staff_obj: any //Used to transfer object to Staff Details Component
  rolesArr = [];
  role = ""; // Used to filter Staff by role
  search = "";
  supervisorId: any;

  constructor(private schoolService: SchoolService, private alertService: AlertService) { }

  ngOnInit() {
    this.getAllStaff();
  }

  updatedTable($event) {
    this.getAllStaff();
  }

  /** Filter Staff on the basis of Roles */
  filterStaff() {
    this.staffArr = this.filter(Object.assign([], this.copyStaffArr));
  }

  /** Actual Filtering by Role */
  filter(array: any[]) {
    let filterStaffArr = [];
    if (this.role) {
      filterStaffArr = array.filter(e => e.roles && e.roles.includes(this.role));
    }
    else {
      filterStaffArr = array;
    }
    return filterStaffArr;
  }

  /** Get List of All Staff Members  */
  getAllStaff() {
    this.staff_loader = true;
    this.schoolService.getStaff().subscribe((res) => {
      this.staffArr = res;
      this.copyStaffArr = Object.assign([], res);
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
  setShowDetails(val: boolean, staff_obj?) {
    this.showDetails = val;
    this.showDetails ? (this.col = "col-8") : (this.col = "col-12");
    if (staff_obj) {
      this.staff_obj = staff_obj;
      this.supervisorId = staff_obj.id;
      console.log(staff_obj);
    }

    if(!val){
      this.supervisorId = "";
    }
  }

}
