import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  user: any;

  constructor(private formBuilder: FormBuilder,
    private router: Router,
    public authService: AuthService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: [''],
      password: ['']
    });
  }

  onSubmit() {
    this.authService.loginUser(this.loginForm.value).subscribe((res) => {
      localStorage.setItem('access_token', res.access_token);
      localStorage.setItem('user_type',res.user_type);
      console.log(res);
      this.getUserInfo();
    });
  }

  getUserInfo() {
    this.authService.getInfo().subscribe((res) => {
      this.user = res,
      this.checkUserType(this.user.userType);
    });
  }

  checkUserType(userType) {
    switch (userType) {
      case "Admin": this.router.navigate(['admin']); break;
      case "Student": this.router.navigate(['student/' + this.user.id + '/home']); break;
      case "Teacher": this.router.navigate(['teacher/' + this.user.id + '/home']); break;
    }
  }
}
