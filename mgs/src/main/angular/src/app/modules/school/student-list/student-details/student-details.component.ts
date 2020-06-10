import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrls: ['./student-details.component.scss']
})
export class StudentDetailsComponent implements OnInit {

  @Input() studentDetails: any;
  imagePath  = "assets/images/childprofile.jpg";
  constructor() { }

  ngOnInit() {
  }

}
