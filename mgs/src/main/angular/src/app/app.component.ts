import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'MGS';


  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {

    if (!JSON.parse(localStorage.getItem('access_token'))) {
      this.router.navigate(['/login']);
    } else {
      this.checkUserType(JSON.parse(localStorage.getItem('user_type')));
    }
  }

  checkUserType(userType) {
    switch (userType) {
      case "MainAdmin": this.router.navigate(['Admin']); break;
      case "Student": this.router.navigate(['Student/' + '/home']); break;
      case "Supervisor": this.router.navigate(['Supervisor/' + '/home']); break;
      case "Coordinator": this.router.navigate(['Coordinator/' + '/home']); break;
      case "Head": this.router.navigate(['Head/' + '/home']); break;
      case "SchoolAdmin": this.router.navigate(['School/' + '']); break;
    }
  }

}
