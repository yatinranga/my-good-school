import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

  constructor(private http: CustomHttpService) { }

  // get Profile of Teacher
  getProfile(teacherId) {
    return this.http.get("/api/teacher/" + teacherId);
  }

  // get PENDING Activities of Teacher
  getPendingActivity(coachId) {
    return this.http.get("/api/coach/activities?coachId=" + coachId + "&status=pending");
  }

  // get PENDING Activities of Teacher
  getReviewedActivity(coachId) {
    return this.http.get("/api/coach/activities?coachId=" + coachId + "&status=reviewed");
  }

  // get ALL Activities of Teacher
  getAllActivity(coachId){
    return this.http.get("/api/coach/activities?coachId=" + coachId );
  }

  // get SAVED Activities of Teacher
  getSavedActivity() {
    return this.http.get("api/coaches/activities");
  }
  
  // get AWARDS of school
  getAwards(schoolId) {
    return this.http.get("/api/award?schoolId=" + schoolId);
  }

  // get all activities in particular school
  getActivity(schoolId) {
    return this.http.get("/activitiesOffered/" + schoolId);
  }

  // get GRADES of school
  getGrades(schoolId) {
    return this.http.get("/grades?schoolId=" + schoolId)
  }

  // SAVE Reviewed Activity
  saveReviewedActivity(formData) {
    return this.http.post("/api/coach/save", formData);
  }

  // SUBMIT Acitivity by Teacher
  submitActivity(activityPerformedId) {
    console.log("service");
    return this.http.post("/api/coach/" + activityPerformedId + "/submit", {});
  }

  // ADD new Award
  addAward(formData) {
    return this.http.post("/api/award", formData);
  }

  // get LIST of students who performed specific activity of particular grade
  getStudents(schoolId, gradeId, activityId, teacherId) {
    return this.http.get("/api/student/activity/" + activityId + "?schoolId=" + schoolId + "&gradeId=" + gradeId + "&teacherId=" + teacherId);
  }

  // Assign Award to Students
  assignAward(reqBody){
    return this.http.post("api/award/assign",reqBody);
  }
}
