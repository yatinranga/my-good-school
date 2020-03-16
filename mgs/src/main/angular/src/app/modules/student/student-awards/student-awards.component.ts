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
  psdAreas :  any = "";
  focusAreas :  any = "";

  constructor(private studentService : StudentService) { }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo['student'].id;
    this.schoolId = this.studentInfo['student'].schoolId;
    
    this.getAwards();
    this.getAreas();

    // this.getStudentActivity();
  }

  // get PSD , Focus Area and 4S
  getAreas(){
    // this.studentService.getFocusAreas().subscribe((res) => {
    //   this.focusAreaArr = res;
    // },
    //   (err) => { console.log(err) });

    // this.studentService.getPsdAreas().subscribe((res) => {
    //   this.psdAreaArr = res;
    // },
    //   (err) => { console.log(err) });

    // this.studentService.getFourS().subscribe((res) => {
    //   this.fourSArr = res;
    // },
    //   (err) => { console.log(err) });
    this.studentService.getActivityAreas().subscribe((res) => {
      console.log(res);
      this.psdAreaArr = res["PSD AREAS"]
      this.focusAreaArr = res["Focus Areas"]
      this.fourSArr = res["Four S"]
      console.log(this.fourSArr);

    },
    (err) => {console.log(err);});
  }

  // getStudentActivity() {
  //   this.studentService.getActivity(this.schoolId).subscribe(
  //     (res) => this.activities = res ,
  //     (err) => console.log(err)
  //   );
  // }

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
      filterAwardsArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.activity.fourS == this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas));
      this.loader = false;
    } 
    else if (this.psdAreas && this.fourS) {
      filterAwardsArr = array.filter(e => e.psdAreas && e.psdAreas.includes(this.psdAreas) && e.activity.fourS == this.fourS);
      this.loader = false;
    } 
    else if (this.fourS && this.focusAreas) {
      filterAwardsArr = array.filter(e => e.activity.fourS == this.fourS && e.focusAreas && e.focusAreas.includes(this.focusAreas));
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
      filterAwardsArr = array.filter(e => e.activity.fourS == this.fourS);
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


  // getStudentAwards(activityId){
  //   this.loader = true;
  //   console.log(activityId);

  //   if(activityId === "All"){
  //     this.studentService.getAllAwards(this.studentId).subscribe((res) => {
  //       this.awardsArr = res;
  //       console.log(res);
  //       this.loader = false;
  //     },
  //     (err) =>  {console.log(err)
  //     this.loader = false });
  //   }else{
  //     this.studentService.getAwards(this.studentId,activityId).subscribe((res) => {
  //       this.awardsArr = res;
  //       console.log(res);
  //       this.loader = false;
  //     },
  //     (err) =>  {console.log(err)
  //       this.loader = false });
  //   }
  // }

  getDate(date) {
    return new Date(date)
  }
}
