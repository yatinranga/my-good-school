import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http: CustomHttpService) { }

  // Single student signup
  uploadStudentDetails(data) {
    const url = "/students/signUp"
    return this.http.post(url, data);
  }

  uploadTeacherDetails(data) {
    const url = "/teachers/signUp"
    return this.http.post(url, data);
  }

  // to get profile of student
  getProfile(studentId) {
    return this.http.get("/api/students/" + studentId);
  }

  //  to get info about the student
  getStudentInfo() {
    return this.http.get("/api/info");
  }

  // to get the list of all schools
  getSchools(url) {
    return this.http.get(url);
  }

  // to get activities in particular school
  getActivity(schoolId) {
    return this.http.get("/activitiesOffered/" + schoolId);
  }

  // to get SAVED activities of student
  getSavedActivity(studentCid) {
    return this.http.get("/api/students/activities?status=saved&studentId=" + studentCid);
  }

  // to get SUBMITTED Activities of student
  getSubmittedActivity(studentCid) {
    return this.http.get("/api/students/activities?status=submitted&studentId=" + studentCid);
  }

  // to get SUBMITTED Activities of student
  getReviewedActivity(studentCid) {
    return this.http.get("/api/students/activities?status=reviewed&studentId=" + studentCid);
  }

  // to get ALL Activities of student
  getAllActivity(studentCid) {
    return this.http.get("/api/students/activities?studentId=" + studentCid);
  }

  // to get teacher/coaches who perform particular activity
  getCoach(schoolId, activityId) {
    return this.http.get("/api/coaches/" + schoolId + "/" + activityId);
  }

  // to ADD new activity performed by student
  addActivity(url, formData: FormData) {
    return this.http.post(url, formData)
  }

  // to SUBMIT the performed activity by student
  submitActivity(activityPerformedId) {
    return this.http.post("/api/students/" + activityPerformedId + "/submit", {});    
  }
}
