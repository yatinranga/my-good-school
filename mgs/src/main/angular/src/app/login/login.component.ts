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

  constructor(private fb: FormBuilder,
    private router: Router,
    public authService: AuthService) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      username: [''],
      password: ['']
    });
  }

  onSubmit() {
    this.authService.loginUser(this.loginForm.value)
      .subscribe(
        res => {
            console.log(res),
            localStorage.setItem('access_token',res.access_token),
            this.router.navigate(['admin'])
        },
        err => console.log(err)
      );
  }

  // gotoSignup(){
  //   this.router.navigate(['/signup/student'])
  // }

}
