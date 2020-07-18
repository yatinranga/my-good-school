import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';
import { BASE_URL } from 'src/app/services/app.constant';

@Component({
  selector: 'app-student-awards',
  templateUrl: './student-awards.component.html',
  styleUrls: ['./student-awards.component.scss']
})
export class StudentAwardsComponent implements OnInit {
  BASE_URL: string;
  studentInfo: any;
  schoolId: any;
  studentId: any;
  awardsArr = [];
  copyAwardArr: any = [];
  loader: boolean = false;

  focusAreaArr = [];
  psdAreaArr = []
  fourSArr = [];

  fourS: any = "";
  psdAreas: any = "";
  focusAreas: any = "";

  constructor(private studentService: StudentService) { 
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
  }

  ngOnInit() {
    this.studentInfo = JSON.parse(localStorage.getItem('user_info'));
    this.studentId = this.studentInfo.id;
    this.schoolId = this.studentInfo.schoolId;

    this.getAwards();
    this.getAreas();
  }

  // get PSD , Focus Area and 4S
  getAreas() {
    this.studentService.getActivityAreas().subscribe((res) => {
      this.psdAreaArr = res["PSD Areas"];
      this.focusAreaArr = res["Focus Areas"];
      this.fourSArr = res["Four S"];
    },
      (err) => { console.log(err); });
  }

  // get All awards of Student
  getAwards() {
    this.loader = true;
    this.studentService.getAllAwards(this.studentId).subscribe((res) => {
      this.awardsArr = res;
      this.copyAwardArr = Object.assign([], res);
      console.log(res);
      this.loader = false;
    },
      (err) => {
        console.log(err)
        this.loader = false
      });
  }

  // Filter Awards 
  filterAwards = () => {
    this.awardsArr = this.filter(Object.assign([], this.copyAwardArr));
  }

  // Actual Filterting of Awards on the basis of selected criteria
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
      filterAwardsArr = array.filter(e => e.criterionValue == this.psdAreas);
      this.loader = false;
    }
    else if (this.fourS) {
      filterAwardsArr = array.filter(e => e.criterionValue == this.fourS);
      this.loader = false;
    }
    else if (this.focusAreas) {
      filterAwardsArr = array.filter(e => e.criterionValue == this.focusAreas);
      this.loader = false;
    }
    else {
      filterAwardsArr = array;
    }
    return filterAwardsArr;
  }

  countStar(count){
    const array = [];
    var mode = count;
    for (var index = 0; index < Math.floor(count); index++) {
      array[index] = index+1;
    }
    if(mode.toString().split('.')[1]) array[index] = parseFloat('0.'+ mode.toString().split('.')[1]);
    return array;
  }

  star(star){
    switch (star) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
        return 'fa fa-star'
        break;
        case 0.5:
          return 'fa fa-star-half'
        break;
    }
  }


}
