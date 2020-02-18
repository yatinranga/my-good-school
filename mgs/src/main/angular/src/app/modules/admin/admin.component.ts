// import { Component, OnInit } from '@angular/core';
// import { AdminService } from './admin.service';

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AdminService } from '../../services/admin.service';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})

export class AdminComponent implements OnInit {

  constructor(public authService: AuthService, private router: Router) { }

  ngOnInit() { }

}



  // showStudentUpload() {
  //   this.router.navigate(['/admin/student-upload']);
  // }

  // showTeacherUpload() {
  //   this.router.navigate(['/admin/teacher-upload']);
  //  }


  //   downloadTeacher(){
  //     this.dwnld.nativeElement.href = "http://localhost:8083/api/template/export/teacher";
  //     let a = this.dwnld.nativeElement.click();
  //     window.alert("Download Successful !!");    
  //   }

  //   downloadCoach(){
  //     this.dwnld.nativeElement.href = "http://localhost:8083/api/template/export/coach";
  //     this.dwnld.nativeElement.click();
  //     window.alert("Download Successful !!");
  //   }
