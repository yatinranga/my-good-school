import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { FormBuilder, FormGroup } from '@angular/forms';
declare let $: any;

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  usersArr: any = [];
  rolesArr: any = [];
  roleName: string = "";
  authoritiesArr: any = [];
  editUserForm: FormGroup;
  user_loader: boolean = false;

  constructor(private adminService: AdminService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.getUsers();

    this.editUserForm = this.formBuilder.group({
      id: [null],
      name: [null],
      username: [null],
      email: [null],
      contactNumber: [null],
      roleIds: [null],
      schoolId: [null]
    })
  }

  getUsers() {
    this.user_loader = true;
    this.adminService.getUsers().subscribe((res) => {
      console.log(res);
      this.usersArr = res;
      this.user_loader = false;

    }, (err) => {
      console.log(err);
      this.user_loader = false;
    });
  }

  editUser(user) {
    console.log(user);
    $('#editUserModal').modal('show');
    $('#editUserModal').modal({
      backdrop: 'static',
      keyboard: false
    });
    this.editUserForm.patchValue({
      id: user.id,
      name: user.name,
      username: user.username,
      email: user.email,
      contactNumber: user.contactNumber,
      // roleIds:user.
      schoolId: user.schoolId
    })

    this.getRoles();
  }

  getRoles() {
    this.adminService.getRoles().subscribe((res) => {
      this.rolesArr = res;
      console.log(res);
    }, (err) => { console.log(err); })
  }

  getAuthorities(roleName) {
    console.log("this is called");
    console.log(roleName);

  }

  // stop toogle of table
  stopCollapse(e) {
    e.stopPropagation();
  }

  // Reset Form
  resetForm() {
    this.editUserForm.reset();
  }

}
