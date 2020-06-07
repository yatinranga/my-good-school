import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {

  createUser: FormGroup;
  rolesArr:[] = [];

  constructor(private adminService: AdminService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.getRoles();

    this.createUser = this.formBuilder.group({
      
    })
  }

  getRoles(){
    this.adminService.getRoles().subscribe((res)=>{
      console.log(res);
    },(err) =>{
      console.log(err);
    });
  }

}
