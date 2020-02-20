import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  showActivityList = false;
  constructor(public authService : AuthService) { }

  ngOnInit() {
  }

  onActivityClick() {
    this.showActivityList = !this.showActivityList;
  }
}
