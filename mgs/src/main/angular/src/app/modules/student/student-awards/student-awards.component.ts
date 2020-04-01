import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-student-awards',
  templateUrl: './student-awards.component.html',
  styleUrls: ['./student-awards.component.scss']
})
export class StudentAwardsComponent implements OnInit {
  studentInfo: any;
  schoolId: any;
  activities: any;
  studentId: any;
  awardsArr = [];
  activityId = "All";
  copyAwardArr : any = [];
  loader : boolean = false ;

  focusAreaArr = [];
  psdAreaArr = []
  fourSArr =[];

  fourS :  any = "";
  psdAreas : any = "";
  focusAreas : any = "";

  constructor(private studentService : StudentService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.schoolId = this.studentInfo['student'].schoolId;
    
    this.getAwards();
    this.getAreas();
  }

  // get PSD , Focus Area and 4S
  getAreas(){
    this.studentService.getActivityAreas().subscribe((res) => {
      this.psdAreaArr = res["PSD Areas"];
      this.focusAreaArr = res["Focus Areas"];
      this.fourSArr = res["Four S"];
    },
    (err) => {console.log(err);});
  }

  // get All awards of Student
  getAwards(){
    this.loader = true;
    this.studentService.getAllAwards(this.studentId).subscribe((res) => {
      this.awardsArr = res;
      this.copyAwardArr = Object.assign([],res);

      console.log(res);
      this.loader = false;
    },
    (err) =>  {console.log(err)
    this.loader = false });
  }

  filterAwards = () => {
    this.awardsArr = this.filter(Object.assign([],this.copyAwardArr));
  }

  filter(array: any[]) {
    this.loader = true;
    let filterAwardsArr = [];
    if (this.psdAreas && this.fourS && this.focusAreas) {
      filterAwardsArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.fourS == this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas));
      this.loader = false;
    } 
    else if (this.psdAreas && this.fourS) {
      filterAwardsArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.fourS == this.fourS);
      this.loader = false;
    } 
    else if (this.fourS && this.focusAreas) {
      filterAwardsArr = array.filter(e => e.fourS == this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas));
      this.loader = false; 
    } 
    else if (this.psdAreas && this.focusAreas) {
      filterAwardsArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.focusAreas && e.focusAreas.includes(this.focusAreas));
      this.loader = false;
    } 
    else if (this.psdAreas) {
      filterAwardsArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas));
      this.loader = false;
    } 
    else if (this.fourS) {
      filterAwardsArr = array.filter(e => e.fourS == this.fourS);
      this.loader = false;
    } 
    else if (this.focusAreas) {
      filterAwardsArr = array.filter(e => e.focusAreas && e.focusAreas.includes(this.focusAreas));
      this.loader = false;
    } 
    else {
      filterAwardsArr = array;
    }    
    return filterAwardsArr;    
  }

  getDate(date) {
    return new Date(date)
  }
}
