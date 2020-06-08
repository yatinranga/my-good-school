import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BASE_URL } from 'src/app/services/app.constant';
import { AlertService } from 'src/app/services/alert.service';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-supervisor-bulk-upload',
  templateUrl: './supervisor-bulk-upload.component.html',
  styleUrls: ['./supervisor-bulk-upload.component.scss']
})
export class SupervisorBulkUploadComponent implements OnInit {

  @ViewChild('dwnld', { static: false }) dwnld: ElementRef;

  schoolInfo: any;
  teacherBulkForm: FormGroup;
  files: any = [];

  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder,
    private alertService: AlertService) { }

  ngOnInit() {
    this.schoolInfo = JSON.parse(localStorage.getItem("user_info"));

    this.teacherBulkForm = this.formBuilder.group({
      type: ['teacher'],
      schoolId: [this.schoolInfo.schoolId],
      selectedFile: ['']
    });
  }



  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.files = [];
      const file = event.target.files[0];
      this.files = [...event.target.files];
      this.teacherBulkForm.value['selectedFile'] = event.target.files[0];
      console.log(this.files);
    }
  }

  uploadTeacher() {
    if (this.teacherBulkForm.value.schoolId && (this.files != 0)) {
      this.alertService.showLoader("");
      const formData = new FormData();
      formData.append('file', this.teacherBulkForm.value.selectedFile);
      formData.append('type', this.teacherBulkForm.value.type);
      formData.append('schoolId', this.teacherBulkForm.value.schoolId);
      console.log(this.teacherBulkForm.value);

      this.schoolService.UploadExcel("/api/template/bulkUpload", formData).subscribe(
        (res) => {
          console.log(res);
          this.alertService.showSuccessToast('Uploaded Successfully');
          this.teacherBulkForm.value.selectedFile = [];
        }, (err) => {
          console.log(err);
          if (err.status == 400) {
            this.alertService.showMessageWithSym(err.msg, "", "info");
          }
          else {
            this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
          }
        });

    } else {
      this.alertService.showErrorAlert("Please fill all the columns");
    }
  }

  downloadTeacher() {
    // this.dwnld.nativeElement.href = BASE_URL + "/api/template/export?type=teacher&access_token=" + localStorage.getItem('access_token');
    this.dwnld.nativeElement.href = BASE_URL + "/template/export?type=teacher";
    this.dwnld.nativeElement.click();
  }

  removeFile() {
    this.files = [];
    this.teacherBulkForm.value.selectedFile = [];
  }

}
