import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-saved-actitvity',
  templateUrl: './saved-actitvity.component.html',
  styleUrls: ['./saved-actitvity.component.scss']
})
export class SavedActitvityComponent implements OnInit {

  studentInfo = [];
  savedActivitiesArr = [];
  studentID = "";
  editActivity = false;
  savedActivityForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private studentService: StudentService) { }

  ngOnInit() {
    this.studentService.getStudentInfo().subscribe((res) => {
      this.studentInfo = res;
      this.studentID = res.student.id;
      this.getStudentSavedActivities(this.studentID)
    },
      (err) => console.log(err))

    this.savedActivityForm = this.formBuilder.group({
      savedActivityId: [''],
      savedActivityDetails: [''],
      savedActivityDate: [''],
      savedCoachId: [''],
      attachment: ['']
    })
  }

  getStudentSavedActivities(studentId) {
    this.studentService.getSavedActivity(studentId).subscribe((res) => {
      this.savedActivitiesArr = res
      console.log(this.savedActivitiesArr);
      console.log("Date : ", this.savedActivitiesArr[1].dateOfActivity);
    },
      (err) => console.log(err));
  }

  editSelectedActivities(index) {
    console.log(this.savedActivitiesArr[index]);
    console.log(this.editActivity);
    return this.editActivity = !this.editActivity;
    
  }

  updateActivity(){

  }

  submitActivity() {
    console.log(this.savedActivityForm.value);
  }
}