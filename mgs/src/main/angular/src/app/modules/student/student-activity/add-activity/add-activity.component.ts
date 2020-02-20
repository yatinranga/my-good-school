import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-add-activity',
  templateUrl: './add-activity.component.html',
  styleUrls: ['./add-activity.component.scss']
})
export class AddActivityComponent implements OnInit {

  studentInfo: any = [];
  schoolId = "";
  activityId = "";
  activities = [];
  coaches = [];
  addActivityForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private studentService: StudentService,
    private alertService: AlertService) { }

  ngOnInit() {
    this.studentService.getStudentInfo().subscribe((res) => {
      console.log(res);
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
      (res) => { this.activities = res; console.log(this.activities); },
      (err) => console.log(err)
    );
  }

  getStudentCoach(activityId) {
    console.log(activityId);
    this.studentService.getCoach(this.schoolId, activityId).subscribe(
      (res) => { console.log(res); this.coaches = res },
      (err) => console.log(err)
    );
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.addActivityForm.value['attachment'] = file;
      console.log(this.addActivityForm.value['attachment']);
    }
  }

  saveActivity() {
    const formData = new FormData();
    formData.append('studentId', this.studentInfo.student.id);
    formData.append('activityId', this.addActivityForm.value.addActivityId);
    formData.append('coachId', this.addActivityForm.value.addCoachId);
    formData.append('dateOfActivity', this.addActivityForm.value.addActivityDate);
    formData.append('fileRequests', this.addActivityForm.value.attachment);
    // formData.append('id', this.addActivityForm.value.schoolId);
    console.log(formData);
    console.log(this.addActivityForm.value);

    this.studentService.addActivity("/api/students/activities", formData).subscribe(
      (res) => {
        console.log(res);
        this.alertService.showSuccessToast('Activity Saved !');
      },
      (err) => console.log(err)
    );
  }

}

// const formData = new FormData();
// formData.append('studentId', this.addActivityForm.value.selectedFile);
// formData.append('activityId', this.addActivityForm.value.type);
// formData.append('coachId', this.addActivityForm.value.schoolId);
// formData.append('dateOfActivity', this.addActivityForm.value.schoolId);
// formData.append('fileRequests', this.addActivityForm.value.schoolId);
// formData.append('id', this.addActivityForm.value.schoolId);
// console.log(formData);