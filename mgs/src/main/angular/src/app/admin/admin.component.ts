// import { Component, OnInit } from '@angular/core';
// import { AdminService } from './admin.service';

import { Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';  
import { HttpClient } from '@angular/common/http';  
import { AdminService } from './admin.service';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})

export class AdminComponent implements OnInit {  

  uploadForm = this.fb.group({
    profile : ['']
  }) ;

  constructor(private adminService: AdminService,
              private fb : FormBuilder,
              private http : HttpClient) { }

  ngOnInit() {
  }

  onFileSelect(event){
    if(event.target.files.length>0){
      const file = event.target.files[0];
      this.uploadForm.get('profile').setValue(file);
    }
  }

  onSubmit(){
    const formData = new FormData();
    formData.append('file',this.uploadForm.get('profile').value);
    console.log(this.uploadForm.get('profile').value);

    this.adminService.UploadExcel(formData).subscribe(
      (res) => console.log(res),
      (err) => console.log(err)
    );
  }

  downloadStudent(){
    this.adminService.getStudent().subscribe(
      (res) => console.log(res),
      (err) => console.log(err)
    );
  }
}
