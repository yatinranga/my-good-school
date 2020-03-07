import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SchoolService } from 'src/app/services/school.service';
import { AlertService } from 'src/app/services/alert.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-school-signup',
  templateUrl: './school-signup.component.html',
  styleUrls: ['./school-signup.component.scss']
})
export class SchoolSignupComponent implements OnInit {

  schoolSignup: FormGroup;
  path = "";
  generalActivitiesArr = [];

  constructor(private formBuilder: FormBuilder, private schoolService: SchoolService,
     private alertService: AlertService, private router: Router) { }

  ngOnInit() {
    this.schoolSignup = this.formBuilder.group({
      name: [null, [Validators.required]],
      address: [null, [Validators.required]],
      email: [null, [Validators.email, Validators.required]],
      contactNo: [null],
      logo: [null],
      generalActivities: ['']
    })

    this.schoolService.getGeneralActivities().subscribe((res) => {
      console.log(res);
      this.generalActivitiesArr = res;
    },
    (err) => console.log(err));        
  }

  onImageSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.schoolSignup.value.logo = file;

      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);

      reader.onload = (event: any) => { this.path = event.target.result; }
    } else {
      this.path = null;
    }
  }


  onSubmit() {
    console.log(this.schoolSignup.value);
    const formData = new FormData();
    
    Object.keys(this.schoolSignup.value).forEach(key => {
      if(key=='generalActivities'){
        if(typeof(this.schoolSignup.value[key])=='object'){
          this.schoolSignup.value.generalActivities.forEach((element, index) => {
            formData.append(key + '[' + index + ']', element);
          });
        }
      }else {
        formData.append(key, this.schoolSignup.value[key])
      }
    });

    // formData.append('name',this.schoolSignup.value.name);
    // formData.append('address',this.schoolSignup.value.address);
    // formData.append('email',this.schoolSignup.value.email);
    // formData.append('contactNo',this.schoolSignup.value.contactNo);
    // formData.append('logo',this.schoolSignup.value.logo);
    // formData.append('generalActivities',this.schoolSignup.value.generalActivities);    



    this.schoolService.schoolSignup(formData).subscribe((res) => {
      console.log(res);
    this.alertService.showSuccessToast('SignUp Successfully');
    this.router.navigate(['./login'])
    },
    (err) => console.log(err));
  }

}
