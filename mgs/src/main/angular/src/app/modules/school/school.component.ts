import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-school',
  templateUrl: './school.component.html',
  styleUrls: ['./school.component.scss']
})
export class SchoolComponent implements OnInit {


  showSidebar: boolean = false;

  constructor(public authService: AuthService) { }

  ngOnInit() {
  }

  setShowSidebar(){
    this.showSidebar = !this.showSidebar;
  }

}
