import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { BASE_URL } from '../../../services/app.constant';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-student-upload',
  templateUrl: './student-upload.component.html',
  styleUrls: ['./student-upload.component.scss']
})
export class StudentUploadComponent implements OnInit, AfterViewInit {

  @ViewChild('dwnld', { static: true }) dwnld: ElementRef

  schools = [];

  studentBulkForm : FormGroup;
  files: any = [];

  constructor(private adminService: AdminService,
    private formBuilder: FormBuilder,
    private alertService: AlertService) { }

  ngOnInit() {
    this.adminService.getSchools("/schools").
      subscribe(
        res => this.schools = res,
        err => console.log(err));

    this.studentBulkForm = this.formBuilder.group({
      type: ['student'],
      schoolId: ['',Validators.required],
      selectedFile: ['',Validators.required]
    });
  }

  ngAfterViewInit() {

      const header = document.getElementById('myDIV');
      // const btns = header.getElementsByClassName('nav-item');
      // // tslint:disable-next-line:prefer-for-of
      // for (let i = 0; i < btns.length; i++) {
      //   btns[i].addEventListener('click', function() {
      //     const current = document.getElementsByClassName('active');
      //     current[0].className = current[0].className.replace(' active', '');
      //     this.className += ' active';
      //   });
      // }
    
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
    if (this.studentBulkForm.value.schoolId && (this.files!=0) ) {
      this.alertService.showLoader("");
      const formData = new FormData();
      formData.append('file', this.studentBulkForm.value.selectedFile);
      formData.append('type', this.studentBulkForm.value.type);
      formData.append('schoolId', this.studentBulkForm.value.schoolId);
      console.log(this.studentBulkForm.value);

      this.adminService.UploadExcel("/api/template/bulkUpload", formData).subscribe(
        (res) => {
          console.log(res);
          this.alertService.showSuccessToast('Uploaded Successfully');
          this.studentBulkForm.value.selectedFile = [];
        },(err) => { console.log(err) });
        
    } else {
      this.alertService.showErrorAlert("Please fill all the columns");
    }

  }

  downloadStudent() {
    // this.dwnld.nativeElement.href = BASE_URL + "/api/template/export?type=student&access_token=" + localStorage.getItem('access_token');
    this.dwnld.nativeElement.href = BASE_URL + "/template/export?type=student";
    this.dwnld.nativeElement.click();
  }

  removeFile(){
    this.files = [];
    this.studentBulkForm.value.selectedFile = [];
  }
  
}
