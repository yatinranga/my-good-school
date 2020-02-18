import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { AuthService } from 'src/app/services/auth.service';
import { BASE_URL } from 'src/app/services/app.constant';


@Component({
  selector: 'app-teacher-upload',
  templateUrl: './teacher-upload.component.html',
  styleUrls: ['./teacher-upload.component.scss']
})
export class TeacherUploadComponent implements OnInit {

  @ViewChild('dwnld', { static: false }) dwnld: ElementRef;

  schools = [];

  uploadTeacher = this.fb.group({
    school_name : [''],
    selectedFile : ['']
  }) ;

  constructor(private adminService: AdminService,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.adminService.getSchools("schools").
      subscribe(
        res => this.schools = res,
        err => console.log(err))
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.uploadTeacher.get('selectedFile').setValue(file);
    }
  }

  onSubmit() {
    const formData = new FormData();
    formData.append('file', this.uploadTeacher.get('selectedFile').value);
    console.log(this.uploadTeacher.get('selectedFile').value);

    this.adminService.UploadExcel("/api/students/importTeacher", formData).subscribe(
      res => alert("Upload Successful !"),
      err => console.log(err)
    );
  }

  downloadTeacher() {
    this.dwnld.nativeElement.href = BASE_URL + "/api/template/export?type=student&access_token=" + localStorage.getItem('access_token');
    this.dwnld.nativeElement.click();
  }

}
