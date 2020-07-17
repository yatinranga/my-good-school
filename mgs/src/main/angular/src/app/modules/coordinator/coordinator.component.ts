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

    window.onscroll = function() {scrollFunction()};

    function scrollFunction() {
      if (document.body.scrollTop > 70 || document.documentElement.scrollTop > 70) {
        document.getElementById("details-actions").classList.add("sticky");
      } else {
        document.getElementById("details-actions").classList.remove("sticky");

      }
    }

  }

  setShowSidebar(){
    this.showSidebar = !this.showSidebar;
  }

}
