import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-coordinator',
  templateUrl: './coordinator.component.html',
  styleUrls: ['./coordinator.component.scss']
})
export class CoordinatorComponent implements OnInit {

  showSidebar: boolean = false;

  constructor(public authService: AuthService) { }

  ngOnInit() {
  }

  setShowSidebar(){
    this.showSidebar = !this.showSidebar;
  }

}
