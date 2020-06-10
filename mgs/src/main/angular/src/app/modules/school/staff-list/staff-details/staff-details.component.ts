import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-staff-details',
  templateUrl: './staff-details.component.html',
  styleUrls: ['./staff-details.component.scss']
})
export class StaffDetailsComponent implements OnInit {

  @Input() staffDetails: any;
  imagePath  = "assets/images/childprofile.jpg";

  constructor() { }

  ngOnInit() {
  }

}
