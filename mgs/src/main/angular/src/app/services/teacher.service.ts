import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

  constructor(private http: CustomHttpService) { }

  // get Profile of Teacher
  getProfile(teacherId) {
    return this.http.get("/api/teachers/" + teacherId);
  }

  // get PENDING Activities of Teacher
  getPendingActivity(coachId) {
    return this.http.get("/api/coach/activities?coachId=" + coachId + "&status=pending");
  }

  // get PENDING Activities of Teacher
  getReviewedActivity(coachId) {
    return this.http.get("/api/coaches/activities?coachId=" + coachId + "&status=reviewed");
  }

  // get SAVED Activities of Teacher
  getSavedActivity() {
    return this.http.get("api/coaches/activities");
  }

  // SAVE Reviewed Activity
  saveReviewedActivity(formData) {
    return this.http.post("/api/coach/save", formData);
  }

  // SUBMIT Acitivity by Teacher
  submitActivity(activityPerformedId) {
    return this.http.post("api/coach/" + activityPerformedId + "/submit", {});
  }

  // get AWARDS of school
  getAwards(schoolId) {
    return this.http.get("/api/award?schoolId=" + schoolId);
  }

  // get GRADES of school
  getGrades(schoolId) {
    return this.http.get("/api/grades?schoolId=" + schoolId)
  }

  // ADD new Award
  addAward(formData) {
    return this.http.post("/api/award", formData);
  }

  // get all activities in particular school
  getActivity(schoolId) {
    return this.http.get("/activitiesOffered/" + schoolId);
  }

  // get LIST of students who performed specific activity of particular grade
  getStudents(schoolId, gradeId, activityId, teacherId) {
    return this.http.get("/api/student/activity/" + activityId + "?schoolId" + schoolId + "&gradeId=" + gradeId + "&teacherId=" + teacherId);
  }
}
