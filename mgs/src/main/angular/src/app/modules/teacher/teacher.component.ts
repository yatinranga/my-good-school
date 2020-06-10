import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-teacher',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.scss']
})
export class TeacherComponent implements OnInit {

  showSidebar: boolean = false;

  constructor(public authService: AuthService) { }

  ngOnInit() {
  }

  setShowSidebar(){
    this.showSidebar = !this.showSidebar;
  }

}
