import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { TeacherService } from 'src/app/services/teacher.service';

@Component({
  selector: 'app-teacher-profile',
  templateUrl: './teacher-profile.component.html',
  styleUrls: ['./teacher-profile.component.scss']
})
export class TeacherProfileComponent implements OnInit {
  teacherInfo: any;
  teacherId: any;
  teacherDetails = {};

  constructor(private formbuilder: FormBuilder, private teacherService: TeacherService) { }

  ngOnInit() {
    this.teacherInfo = JSON.parse(localStorage.getItem('user_info'));
    this.teacherId = this.teacherInfo['teacher'].id;
    this.teacherService.getProfile(this.teacherId).subscribe((res) => {
      this.teacherDetails = res;
      console.log(res);
    },
    (err) => console.log(err)
    );
  } 

}
