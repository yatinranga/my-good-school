import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router} from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  studentForm = this.fb.group({
    username: [''],
    password: ['']
  });

  constructor(private fb: FormBuilder, 
              private router:Router) { }

  onSubmit(){
    console.log(this.studentForm.value);
    this.router.navigate(["/student"]);
  }

  ngOnInit() {
  }

}
