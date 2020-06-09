import { Component, OnInit } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit {

  constructor(private schoolService: SchoolService) { }

  ngOnInit() {
    this.getUsers();
  }

  getUsers(){
    this.schoolService.getUsers().subscribe((res) => {
      console.log(res);
    }, (err) => {console.log(err);})
  }

}
