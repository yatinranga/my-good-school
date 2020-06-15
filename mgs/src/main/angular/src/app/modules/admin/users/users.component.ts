import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';
declare let $: any;

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  usersArr: any = [];
  rolesArr: any = [];
  schoolArr: any = [];
  editUserForm: FormGroup;
  user_loader: boolean = false;
  userId = "";

  constructor(private adminService: AdminService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.getUsers();

    this.editUserForm = this.formBuilder.group({
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
    this.userId = user.id;
    $('#editUserModal').modal('show');
    $('#editUserModal').modal({
      backdrop: 'static',
      keyboard: false
    });
    this.editUserForm.patchValue({
      name: user.name,
      username: user.username,
      email: user.email,
      contactNumber: user.contactNumber,
      roleIds:user.roles[0].id,
      schoolId: user.schoolId
    })

    this.getRoles();
    this.getSchools();
  }

  getRoles() {
    this.adminService.getRoles().subscribe((res) => {
      this.rolesArr = res;
      console.log(res);
    }, (err) => { console.log(err); })
  }

  getSchools() {
    this.adminService.getSchools("/schools").subscribe((res) => {
      this.schoolArr = res;
      console.log(res);
    }, (err) => { console.log(err); })
  }

  updateUser() {
    this.editUserForm.value.roleIds = [436];
    console.log(this.editUserForm.value);
    this.adminService.updateUser(this.userId, this.editUserForm.value).subscribe((res) => {
      console.log(res);
    }, (err) => {
      console.log(err);
      if (err.status == 400) {
        this.alertService.showMessageWithSym(err.msg, "", "info");
      }
      else {
        this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
      }
    })
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
