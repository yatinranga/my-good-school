import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  constructor(private adminService: AdminService) { }

  ngOnInit() {
    this.getUsers();
  }

  getUsers(){
    this.adminService.getUsers().subscribe((res) => {
      console.log(res);
    },(err) => {console.log(err)});
  }

}
