import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-add-activity',
  templateUrl: './add-activity.component.html',
  styleUrls: ['./add-activity.component.scss']
})
export class AddActivityComponent implements OnInit {

  activites = ["Yoga", "Badminton", "Judo"];
  teachers = ["Mr.Rakesh", "Mrs.Seema"];
  addActivityForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.addActivityForm = this.formBuilder.group({
      addActivity: [''],
      addActivityDetails: [''],
      addActivityDate: [''],
      addTeacher: [''],
      attachment: ['']
    })
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.addActivityForm.value['attachment'] = file;
      console.log(this.addActivityForm.value['attachment']);
    }
  }

  saveActivity() {
    console.log(this.addActivityForm.value);
  }

}

// const formData = new FormData();
// formData.append('studentId', this.studentBulkForm.value.selectedFile);
// formData.append('activityId', this.studentBulkForm.value.type);
// formData.append('coachId', this.studentBulkForm.value.schoolId);
// formData.append('dateOfActivity', this.studentBulkForm.value.schoolId);
// formData.append('fileRequests', this.studentBulkForm.value.schoolId);
// formData.append('id', this.studentBulkForm.value.schoolId);
// console.log(formData);