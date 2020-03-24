import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { AlertService } from 'src/app/services/alert.service';
import { BASE_URL } from 'src/app/services/app.constant';

@Component({
  selector: 'app-school-upload',
  templateUrl: './school-upload.component.html',
  styleUrls: ['./school-upload.component.scss']
})
export class SchoolUploadComponent implements OnInit {

  @ViewChild('dwnld', { static: false }) dwnld: ElementRef;


  schoolBulkForm : FormGroup;
  files: any[];
  
  constructor(private adminService : AdminService, private formBuilder : FormBuilder, private alertSerivce :  AlertService) { }

  ngOnInit() {
    this.schoolBulkForm = this.formBuilder.group({
      type : ['school'],
      selectedFile: ['']
    })
  }
  
  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.files = [];
      const file = event.target.files[0];
      this.files = [...event.target.files];
    }
  }
  
  uploadSchool(){
    const formData =  new FormData();

    // this.adminService.UploadExcel("/api/template/bulkUpload",formData).subscribe((res) => {
    //   console.log(res);
    // },
    // (err) => console.log(err));

  }
  
  downloadSchool(){
    // this.dwnld.nativeElement.href = BASE_URL + "/api/template/export?type=school&access_token=" + localStorage.getItem('access_token');
    this.dwnld.nativeElement.href = BASE_URL + "/template/export?type=school";
    this.dwnld.nativeElement.click();
  }

  removeFile(){
    this.files = [];
  }
}
