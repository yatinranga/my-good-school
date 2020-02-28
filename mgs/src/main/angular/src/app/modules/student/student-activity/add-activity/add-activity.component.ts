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
      addActivityId: [''],
      addActivityDetails: [''],
      addActivityDate: [''],
      addCoachId: [''],
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
    let date = new Date(this.addActivityForm.value.addActivityDate);
    let activityDate = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();

    formData.append('studentId', this.studentInfo.student.id);
    formData.append('activityId', this.addActivityForm.value.addActivityId);
    formData.append('coachId', this.addActivityForm.value.addCoachId);
    formData.append('dateOfActivity', activityDate);
    formData.append('description', this.addActivityForm.value.addActivityDetails);

    this.addActivityForm.value.attachment.forEach((element, index) => {
      formData.append('fileRequests[' + index + '].file', element);
      console.log('fileRequests[' + index + '].file', element);
    });

    this.studentService.addActivity("/api/students/activities", formData).subscribe(
      (res) => {
        console.log(res);
        this.alertService.showSuccessToast('Activity Saved !');
        window.location.reload();
      },
      (err) => console.log(err)
    );
  }

}