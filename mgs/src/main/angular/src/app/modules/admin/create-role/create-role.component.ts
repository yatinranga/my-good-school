import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-create-role',
  templateUrl: './create-role.component.html',
  styleUrls: ['./create-role.component.scss']
})
export class CreateRoleComponent implements OnInit {

  authoritiesArr = [];
  schoolsArr = [];
  createRoleForm: FormGroup;
  constructor(private adminService: AdminService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.createRoleForm = this.formBuilder.group({
      name: [, [Validators.required]],
      authorityIds: [, [Validators.required]],
      schoolId: [, [Validators.required]]
    });

    this.getAllAuthorities();
    this.getAllSchools();
  }

  /** Get List of All the Authorities */
  getAllAuthorities() {
    this.adminService.getAuthorities().subscribe((res) => {
      this.authoritiesArr = res;
    }, (err) => { console.log(err); });
  }

  /** Get List of All Schools */
  getAllSchools() {
    this.adminService.getSchools("/schools").subscribe((res) => {
      this.schoolsArr = res;
    }, (err) => { console.log(err); })
  }

  /** Create new Role */
  createNewRole() {
    console.log(this.createRoleForm.value);
    this.adminService.createRole(this.createRoleForm.value).subscribe((res) => {
      console.log(res);
      this.alertService.showMessageWithSym("Role Created !", "Success", "success");
      this.resetForm();
    }, (err) => {
      console.log(err);
      if (err.status == 400) {
        this.alertService.showMessageWithSym(err.msg, "", "info");
      }
      else {
        this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
      }
    });
  }

  /** Reset Form */
  resetForm() {
    this.createRoleForm.reset();
  }

}
