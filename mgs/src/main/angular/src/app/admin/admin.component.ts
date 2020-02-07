// import { Component, OnInit } from '@angular/core';
// import { AdminService } from './admin.service';

import { Component, OnInit, ViewChild, ElementRef} from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';  
import { HttpClient } from '@angular/common/http';  
import { AdminService } from './admin.service';
import { HttpResponse } from '@angular/common/http';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})

export class AdminComponent implements OnInit { 
  
  @ViewChild ('dwnld', {static:false}) dwnld: ElementRef

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
    this.dwnld.nativeElement.href = "http://localhost:8083/api/template/export/student";
    this.dwnld.nativeElement.click();
  //   this.adminService.getStudent().subscribe(
  //     (res) => console.log(res),
  //     (err) => console.log("Error downloading the file"));
  // }

}
  downloadTeacher(){
    this.dwnld.nativeElement.href = "http://localhost:8083/api/template/export/teacher";
    this.dwnld.nativeElement.click();
  }

  
  downloadCoach(){
    this.dwnld.nativeElement.href = "http://localhost:8083/api/template/export/coach";
    this.dwnld.nativeElement.click();
  }

//   this.fileService.downloadFile().subscribe(response => {
//     window.location.href = response.url;}), 
//     error => console.log('Error downloading the file'),

//                () => console.info('File downloaded successfully');
// }
}