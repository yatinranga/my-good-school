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
  userInfo: any;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.userInfo = JSON.parse(localStorage.getItem('user_info'));
    if (this.userInfo) {
      this.checkUserType(this.userInfo.userType);
    }
  }

  // if (this.authService.loggedIn()) {
  //   this.router.navigate(['/admin'])
  // } else {
  //   this.router.navigate(['/login'])
  // }

  checkUserType(userType) {
    if (localStorage.getItem('user_info')) {
      switch (userType) {
        case "Admin": this.router.navigate(['admin']); break;
        case "Student": this.router.navigate(['student/' + this.userInfo.id + '/home']); break;
        case "Teacher": this.router.navigate(['teacher/' + this.userInfo.id + '/home']); break;
      }
    }
  }

}
