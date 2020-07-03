import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
import { BASE_URL } from 'src/app/services/app.constant';

declare let $: any;

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.scss']
})
export class CertificatesComponent implements OnInit {

  BASE_URL: string;

  certificateForm: FormGroup;
  certificatesArr = [];
  fourSArr: any = [];
  files: any[];
  path: any;
  add_loader = false;
  certificate_loader = false; // Loader during get certificates
  certiModalType = "Add";
  certId: any;
  certIndex: any;

  constructor(private formBuilder: FormBuilder, private studentService: StudentService, private alertService: AlertService) {
    this.BASE_URL = BASE_URL + "/file/download?filePath=";
  }

  ngOnInit() {
    this.viewCertificate();
    this.getfourS();

    this.certificateForm = this.formBuilder.group({
      title: [, [Validators.required]],
      description: [, [Validators.required]],
      fourS: [, [Validators.required]],
      certificationAuthority: [, [Validators.required]],
      image: [null],
    })
  }

  viewCertificate() {
    this.certificate_loader = true;
    this.studentService.getCertificates().subscribe((res) => {
      console.log(res);
      this.certificatesArr = res;
      this.certificate_loader = false;
    }, (err) => {
      console.log(err);
      this.certificate_loader = false;
    });
  }

  // get 4s Area
  getfourS() {
    this.studentService.getActivityAreas().subscribe((res) => {
      this.fourSArr = res["Four S"];
    }, (err) => {
      console.log(err);
    });
  }

  // On selecting the File(Certificate)
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

  // add new certificate
  addCertificate() {
    console.log(this.certificateForm.value.image);
    console.log(this.certificateForm.value);

    const formData = new FormData();

    if (this.certiModalType == "Add") {
      formData.append('title', this.certificateForm.value.title);
      formData.append('description', this.certificateForm.value.description);
      formData.append('fourS', this.certificateForm.value.fourS);
      formData.append('certificationAuthority', this.certificateForm.value.certificationAuthority);
      formData.append('image', this.certificateForm.value.image);

      if (this.certificateForm.value.image) {
        this.alertService.confirmWithoutLoader('question', 'Are you sure you want to add certificate ?', '', 'Yes').then(result => {
          if (result.value) {
            this.add_loader = true;
            this.studentService.addCertificate(formData).subscribe((res) => {
              console.log(res);
              this.alertService.showSuccessAlert("");
              $('#certificateModal').modal('hide');
              $('.modal-backdrop').remove();
              this.resetForm();
              this.files = [];
              this.certificatesArr.unshift(res);
              this.add_loader = false;
            }, (err) => {
              console.log(err);
              this.add_loader = false;
            });
          }
        })
      }
      else {
        this.alertService.showMessageWithSym("Please add attachment", "", "info");
      }
    }

    if (this.certiModalType == "Edit") {
      console.log(this.certificateForm.value);
      formData.append('title', this.certificateForm.value.title);
      formData.append('description', this.certificateForm.value.description);
      formData.append('fourS', this.certificateForm.value.fourS);
      formData.append('certificationAuthority', this.certificateForm.value.certificationAuthority);

      // formData.append('image', this.certificateForm.value.image);

      if (this.certificateForm.value.image || this.certificateForm.value.image == null) {

        if (this.certificateForm.value.image)
          formData.append('image', this.certificateForm.value.image);
        this.alertService.confirmWithoutLoader('question', 'Are you sure you want to Update ?', '', 'Yes').then(result => {
          if (result.value) {
            this.add_loader = true;
            this.studentService.updateCertificate(this.certId, formData).subscribe((res) => {
              this.certificatesArr.splice(this.certIndex, 1);
              console.log(res);
              this.alertService.showSuccessAlert("");
              $('#certificateModal').modal('hide');
              $('.modal-backdrop').remove();
              this.resetForm();
              this.files = [];
              this.certificatesArr.unshift(res);
              this.add_loader = false;
            }, (err) => {
              console.log(err);
              this.add_loader = false;
            });
          }
        })
      }
      else {
        this.alertService.showMessageWithSym("Please add attachment", "", "info");
      }
    }
  }

  // edit Certificate
  editCertificatebtn(certi_Obj, index) {

    $('#certificateModal').modal('show');
    this.path = BASE_URL + "/file/download?filePath=" + certi_Obj.imageUrl;
    this.certificateForm.patchValue({
      title: certi_Obj.title,
      description: certi_Obj.description,
      fourS: certi_Obj.fourS,
      certificationAuthority: certi_Obj.certificationAuthority,
    })
    this.certiModalType = "Edit";
    this.certIndex = index;
    this.certId = certi_Obj.id;
    console.log(index);
  }

  // delete Certificate
  deleteCertificate(certi_Obj, index) {
    this.alertService.confirmWithoutLoader('question', 'Are you sure you want to delete ?', '', 'Yes').then(result => {
      if (result.value) {
        this.alertService.showLoader("");
        this.studentService.deleteCertificate(certi_Obj.id).subscribe(res => {
          this.alertService.showMessageWithSym("Certificate Deleted !", "Success", "success");
          this.certificatesArr.splice(index, 1);
        }, (err => {
          this.errorMessage(err);
        }))
      }
    });
  }

  // remove the selected file(certificate)
  removeFile() {
    console.log("Delete Button called");
    this.files = [];
    this.path = "";
    this.certificateForm.value.image = "";
  }

  // reset add certificate form
  resetForm() {
    this.certificateForm.reset();
    this.path = "";
    this.certificateForm.value.image = "";
  }

  // error handling
  errorMessage(err) {
    if (err.status == 400) {
      this.alertService.showMessageWithSym(err.msg, "", "info");
    }
    else {
      this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
    }
  }

  addCertificatebtn() {
    this.certiModalType = "Add";
  }

}
