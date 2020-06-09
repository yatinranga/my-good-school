import { Component, OnInit } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {

  createUserForm: FormGroup;
  rolesArr: [] = [];

  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.createUserForm = this.formBuilder.group({
      id: [null],
      name: [null],
      username: [null],
      email: [null],
      contactNumber: [null],
      roleIds: [null],
      schoolId: [null]
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
    console.log(this.createUserForm.value);
  }

  // Reset Form
  resetForm() {
    this.createUserForm.reset();
  }
}
