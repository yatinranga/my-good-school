import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
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
  userInfo: any;

  constructor(private formBuilder: FormBuilder,
    private router: Router,
    public authService: AuthService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['',[Validators.required]],
      password: ['',[Validators.required]]
    });
  }

  onSubmit() {
    this.authService.loginUser(this.loginForm.value).subscribe((res) => {
      localStorage.setItem('access_token', res.access_token);
      localStorage.setItem('user_type',JSON.stringify(res.user_type));
      console.log(res);
      this.getUserInfo();
    });
  }

  getUserInfo() {
    this.authService.getInfo().subscribe((res) => {
      localStorage.setItem('user_info',JSON.stringify(res));
      this.userInfo = JSON.parse(localStorage.getItem('user_info'));
      this.checkUserType(this.userInfo.userType);
    });
  }

  checkUserType(userType) {
    switch (userType) {
      case "Admin": this.router.navigate(['Admin']); break;
      case "Student": this.router.navigate(['Student/' + '/home']); break;
      case "Teacher": this.router.navigate(['Teacher/' + '/home']); break;
    }
  }
}
