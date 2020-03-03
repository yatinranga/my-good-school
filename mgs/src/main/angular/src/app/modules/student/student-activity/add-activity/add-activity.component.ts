import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';
import { element } from 'protractor';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-activity',
  templateUrl: './add-activity.component.html',
  styleUrls: ['./add-activity.component.scss']
})
export class AddActivityComponent implements OnInit {

  studentInfo: any = [];
  activities = [];
  coaches = [];
  addActivityForm: FormGroup;
  file = [];
  schoolId = "";

  constructor(private formBuilder: FormBuilder, private studentService: StudentService,
    private alertService: AlertService, private router: Router) { }

  ngOnInit() {
    this.studentService.getStudentInfo().subscribe((res) => {
      this.studentInfo = res;
      this.schoolId = res.student.schoolId;
      this.getStudentActivity();
    },
      (err) => console.log(err)
    );

    this.addActivityForm = this.formBuilder.group({
      activityId: [''],
      description: [''],
      dateOfActivity: [''],
      coachId: [''],
      attachment: ['']
    });
  }

  getStudentActivity() {
    this.studentService.getActivity(this.schoolId).subscribe(
      (res) => this.activities = res,
      (err) => console.log(err)
    );
  }

  getStudentCoach(activityId) {
    this.studentService.getCoach(this.schoolId, activityId).subscribe(
      (res) => this.coaches = res,
      (err) => console.log(err)
    );
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      this.file = [...event.target.files];
      this.addActivityForm.value['attachment'] = this.file;
      console.log(this.file);
    }
  }

  saveActivity() {
    const formData = new FormData();
    let date = new Date(this.addActivityForm.value.dateOfActivity);
    let activityDate = date.getFullYear() + "-" + (date.getMonth()) + "-" + date.getDate();

    formData.append('studentId', this.studentInfo.student.id);
    formData.append('activityId', this.addActivityForm.value.activityId);
    formData.append('coachId', this.addActivityForm.value.coachId);
    formData.append('dateOfActivity', activityDate);
    formData.append('description', this.addActivityForm.value.description);

    if (this.addActivityForm.value.attachment.length > 0) {
      this.addActivityForm.value.attachment.forEach((element, index) => {
        formData.append('fileRequests[' + index + '].file', element);
      });
    }

    console.log(this.addActivityForm.value);

    this.studentService.addActivity("/api/student/activities", formData).subscribe(
      (res) => {
        console.log(res);
        this.alertService.showSuccessToast('Activity Saved !');
      },
      (err) => console.log(err)
    );
  }

}