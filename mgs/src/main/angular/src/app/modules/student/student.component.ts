import { Component, OnInit } from '@angular/core';
import { StudentService } from 'src/app/services/student.service';
import { AuthService } from 'src/app/services/auth.service';
declare let $;
@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.scss']
})
export class StudentComponent implements OnInit{

  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    $('button').click(function(){
      $('p:first').addClass('intro');
    });

  }

}
