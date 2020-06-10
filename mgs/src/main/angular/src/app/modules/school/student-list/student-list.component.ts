import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { SchoolService } from 'src/app/services/school.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.scss']
})
export class StudentListComponent implements OnInit {

  constructor(private schoolService: SchoolService, private alertService: AlertService) { }
  col = "col-12";
  showDetails:boolean = false
  studentsArr: any = [];
  student_loader = false;
  student_obj: any; // Used to transfer object to Student Details Component

  ngOnInit() {
    this.getAllStudents();

  }

  /** Set Show Details */
  setShowDetails(val: boolean, student_obj?){
    this.student_obj = student_obj;
    console.log(student_obj);
    this.showDetails = val;
    this.showDetails ? (this.col = "col-6") : (this.col="col-12");
  }

  /** Get List of All Students */
  getAllStudents() {
    this.student_loader = true;
    this.schoolService.getStudents().subscribe((res) => {
      this.studentsArr = res;
      console.log(res);
      this.student_loader = false;

    }, (err) => {
      this.student_loader = false;
      console.log(err);
      if (err.status == 400) {
        this.alertService.showMessageWithSym(err.msg, "", "info");
      }
      else {
        this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
      }
    })
  }

}
