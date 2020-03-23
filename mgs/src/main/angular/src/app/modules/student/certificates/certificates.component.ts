import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
declare let $: any;

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.scss']
})
export class CertificatesComponent implements OnInit {

  certificateForm: FormGroup;
  fourSArr: any = [];
  files: any[];
  path: any;
  add_loader = false;

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) { }

  ngOnInit() {
    this.getfourS();

    this.certificateForm = this.formBuilder.group({
      title: [, [Validators.required]],
      description: [, [Validators.required]],
      fourS: [, [Validators.required]],
      certificationAuthority: [, [Validators.required]],
      image: [, [Validators.required]],
    })
  }

  getfourS() {
    this.studentService.getActivityAreas().subscribe((res) => {
      this.fourSArr = res["Four S"];
    }, (err) => {
      console.log(err);
    });
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.files = [...event.target.files];

      const file = this.files[0];
      this.certificateForm.value['image'] = file;
      console.log(this.certificateForm.value.image);

      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);

      reader.onload = (event: any) => { this.path = event.target.result; }
    } else {
      this.path = null;
    }
  }

  addCertificate() {
    this.add_loader = true;
    console.log(this.certificateForm.value);
    const formData = new FormData();
    formData.append('title', this.certificateForm.value.title);
    formData.append('description', this.certificateForm.value.description);
    formData.append('fourS', this.certificateForm.value.fourS);
    formData.append('certificationAuthority', this.certificateForm.value.certificationAuthority);
    formData.append('image', this.certificateForm.value.image);

    this.alertService.confirmWithoutLoader('question', 'Are you sure you want to add certificate ?', '', 'Yes').then(result => {
      if (result.value) {
        this.studentService.addCertificate(formData).subscribe((res) => {
          console.log(res);
          this.alertService.showSuccessAlert("");
          $('#certificateModal').modal('hide');
          $('.modal-backdrop').remove();
          this.add_loader = false;
        }, (err) => {
          console.log(err);
          this.add_loader = false;
        });
      }
    })
  }

  removeFile(){
    this.files.pop();
    this.files = [];
    this.path="";
  }

  resetForm(){
    this.certificateForm.reset();
  }

}
