import { Component, OnInit } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {
  adminInfo: any;
  createUserForm: FormGroup;
  rolesArr: [] = [];

  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    this.createUserForm = this.formBuilder.group({
      name: [null,[Validators.required]],
      username: [null,[Validators.required]],
      email: [null,[Validators.required]],
      contactNumber: [null,[Validators.required,Validators.minLength(10),Validators.pattern("[0-9]*")]],
      schoolId: [this.adminInfo.schoolId],
      roleIds: [,[Validators.required]]
    });

    this.getRoles();
  }

  /** get all the Roles */
  getRoles() {
    this.schoolService.getRoles().subscribe((res) => {
      this.rolesArr = res;
      console.log(res);
    }, (err) => { console.log(err); })
  }


  /** Create New User */
  addUser() {
    this.alertService.showLoader("");
    this.createUserForm.value.roleIds = [this.createUserForm.value.roleIds];
    console.log(this.createUserForm.value);
    this.schoolService.createUser(this.createUserForm.value).subscribe((res) => {
      console.log(res);
      this.alertService.showMessageWithSym("User Created !","Success","success");
      this.resetForm();
    },(err) => {
      console.log(err);
      this.errorMessage(err);      
    })
  }

  // Reset Form
  resetForm() {
    this.createUserForm.reset();
  }

  /** Handling Error */
  errorMessage(err){
    if (err.status == 400) {
      this.alertService.showMessageWithSym(err.msg, "", "info");
    }
    else {
      this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
    }
  }
}
