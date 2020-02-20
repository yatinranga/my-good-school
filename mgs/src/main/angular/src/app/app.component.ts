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
  userType : any;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if(this.userType = localStorage.getItem('user_type')){
      console.log(this.userType);
      switch (this.userType) {
        case "Admin": this.router.navigate(['admin']); break;
        case "Student": this.router.navigate(['student/' + '/home']); break;
        case "Teacher": this.router.navigate(['teacher/' + '/home']); break;
        case "" : this.router.navigate(['login']); break;
      }
    }
    // if (this.authService.loggedIn()) {
    //   this.router.navigate(['/admin'])
    // } else {
    //   this.router.navigate(['/login'])
    // }
  }

}
