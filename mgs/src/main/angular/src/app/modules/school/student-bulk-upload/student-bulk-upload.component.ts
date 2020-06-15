import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { BASE_URL } from '../../../services/app.constant';
import { AlertService } from 'src/app/services/alert.service';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-student-bulk-upload',
  templateUrl: './student-bulk-upload.component.html',
  styleUrls: ['./student-bulk-upload.component.scss']
})
export class StudentBulkUploadComponent implements OnInit {

  @ViewChild('dwnld', { static: true }) dwnld: ElementRef

  studentBulkForm: FormGroup;
  files: any = [];
  schoolInfo: any;

  constructor(private schoolService: SchoolService,
    private formBuilder: FormBuilder,
    private alertService: AlertService) { }

  ngOnInit() {
    this.schoolInfo = JSON.parse(localStorage.getItem('user_info'));

    this.studentBulkForm = this.formBuilder.group({
      type: ['student'],
      schoolId: [this.schoolInfo.schoolId],
      selectedFile: ['', Validators.required]
    });
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.files = [];
      const file = event.target.files[0];
      this.files = [...event.target.files];

      this.studentBulkForm.value['selectedFile'] = event.target.files[0];
      console.log(this.files);
    }
  }

  uploadStudents() {
    if (this.studentBulkForm.value.schoolId && (this.files != 0)) {
      this.alertService.showLoader("");
      const formData = new FormData();
      formData.append('file', this.studentBulkForm.value.selectedFile);
      formData.append('type', this.studentBulkForm.value.type);
      formData.append('schoolId', this.studentBulkForm.value.schoolId);
      console.log(this.studentBulkForm.value);

      this.schoolService.UploadExcel("/api/template/bulkUpload", formData).subscribe(
        (res) => {
          console.log(res);
          this.alertService.showSuccessToast('Uploaded Successfully');
          this.studentBulkForm.value.selectedFile = [];
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
    else {
      this.alertService.showErrorAlert("Please fill all the columns");
    }

  }

  downloadStudent() {
    // this.dwnld.nativeElement.href = BASE_URL + "/api/template/export?type=student&access_token=" + localStorage.getItem('access_token');
    this.dwnld.nativeElement.href = BASE_URL + "/template/export?type=student";
    this.dwnld.nativeElement.click();
  }

  removeFile() {
    this.files = [];
    this.studentBulkForm.value.selectedFile = [];
  }
}
