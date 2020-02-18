import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
// import { CustomHttpService } from 'src/app/services/custom-http-service.service';
import { HttpClient } from '@angular/common/http';
import { BASE_URL } from '../../../services/app.constant';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-student-upload',
  templateUrl: './student-upload.component.html',
  styleUrls: ['./student-upload.component.scss']
})
export class StudentUploadComponent implements OnInit {

  @ViewChild('dwnld', { static: true }) dwnld: ElementRef

  schools = [];

  studentForm: FormGroup;

  constructor(private adminService: AdminService,
    private formBuilder: FormBuilder,
    private alertService: AlertService) { }

  ngOnInit() {
    this.adminService.getSchools("/schools").
      subscribe(
        res => this.schools = res,
        err => console.log(err));

    this.studentForm = this.formBuilder.group({
      type: ['student'],
      schoolId: [''],
      selectedFile: ['']
    });
  }

  getSchoolID(schoolName) {
    this.studentForm.value['schoolId'] = this.schools.filter((ele) => ele.name === schoolName)[0].id;
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.studentForm.value['selectedFile'] = file;
    }
  }

  uploadStudents() {
    const formData = new FormData();
    formData.append('file', this.studentForm.value.selectedFile);
    formData.append('type', this.studentForm.value.type);
    formData.append('schoolId', this.studentForm.value.schoolId);
    console.log(formData);

    this.adminService.UploadExcel("/api/template/bulkUpload", formData).subscribe(
      (res) => {
        console.log(res);
        this.alertService.showSuccessToast('Uploaded Successfully');
      },
      (err) => console.log(err)
    );
  }


  downloadStudent() {
    this.dwnld.nativeElement.href = BASE_URL + "/api/template/export?type=student&access_token=" + localStorage.getItem('access_token');
    this.dwnld.nativeElement.click();
  }
}
