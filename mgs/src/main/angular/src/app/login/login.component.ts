import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  user: any;
  userInfo: any;
  loader: boolean = false;

  constructor(private formBuilder: FormBuilder, private alertService: AlertService,
    private router: Router,
    public authService: AuthService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    this.loader = true;
    this.authService.loginUser(this.loginForm.value).subscribe((res) => {
      localStorage.setItem('access_token', res.access_token);
      localStorage.setItem('user_type', JSON.stringify(res.user_role[0].name));
      this.getUserInfo();
    }, (err) => {
      this.loader = false;
    });
  }

  getUserInfo() {
    this.authService.getInfo().subscribe((res) => {
      localStorage.setItem('user_info', JSON.stringify(res));
      this.userInfo = JSON.parse(localStorage.getItem('user_info'));
      this.loader = false;
      this.checkUserType(JSON.parse(localStorage.getItem('user_type')));
    }, (err) => {
      this.loader = false;
    });
  }

  checkUserType(userType) {
    switch (userType) {
      case "MainAdmin": this.router.navigate(['Admin']); break;
      case "Student": this.router.navigate(['Student/' + '/home']); break;
      case "Supervisor": this.router.navigate(['Supervisor/' + '/home']); break;
      case "Coordinator": this.router.navigate(['Supervisor/' + '/home']); break;
      case "SchoolAdmin": this.router.navigate(['School/' + '/home']); break;
    }
  }

  forgetPassword() {
    if (this.loginForm.value.username) {
      this.alertService.confirmWithoutLoader('question', 'Are you sure you want to continue?', '', 'Confirm').then(result => {
        if (result.value) {
          console.log(this.loginForm.value.username);
          this.alertService.showLoader("");
          this.authService.forgetPassword(this.loginForm.value.username).subscribe((res) => {
            console.log(res);
            this.alertService.showSuccessAlert("New generated password has been sent to your email.");
          }, (err) => { console.log(err); });
        }
      });
    } else {
      this.alertService.showErrorAlert('Please Enter Username');
    }
  }

}
