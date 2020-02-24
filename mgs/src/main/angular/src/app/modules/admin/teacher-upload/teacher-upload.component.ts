import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { BASE_URL } from 'src/app/services/app.constant';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-teacher-upload',
  templateUrl: './teacher-upload.component.html',
  styleUrls: ['./teacher-upload.component.scss']
})

export class TeacherUploadComponent implements OnInit {

  @ViewChild('dwnld', { static: false }) dwnld: ElementRef;

  schools = [];
  teacherBulkForm : FormGroup;

  constructor(private adminService: AdminService, private formBuilder: FormBuilder,
              private alertService: AlertService) { }

  ngOnInit() {
    this.adminService.getSchools("/schools").subscribe(
      (res) => { this.schools = res },
      (err) => console.log(err));

    this.teacherBulkForm = this.formBuilder.group({
      type: ['teacher'],
      schoolId: [''],
      selectedFile: ['']
    });
  }

 

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.teacherBulkForm.get('selectedFile').setValue(file);
    }
  }

  uploadTeacher() {
    const formData = new FormData();
    formData.append('file', this.teacherBulkForm.value.selectedFile);
    formData.append('type', this.teacherBulkForm.value.type);
    formData.append('schoolId', this.teacherBulkForm.value.schoolId);
    console.log(formData);

    this.adminService.UploadExcel("/api/template/bulkUpload", formData).subscribe(
      (res) => {
        console.log(res);
        this.alertService.showSuccessToast('Uploaded Successfully');
      },
      (err) => console.log(err)
    );
  }

  downloadTeacher() {
    this.dwnld.nativeElement.href = BASE_URL + "/api/template/export?type=teacher&access_token=" + localStorage.getItem('access_token');
    this.dwnld.nativeElement.click();
  }

}