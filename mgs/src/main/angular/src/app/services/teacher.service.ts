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
  getAwards() {
    return this.http.get("/awardTypes");
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
  getStudents(gradeId) {
    return this.http.get("/api/students/?gradeId=" + gradeId);
  }

  // Assign Award to Students
  assignAward(reqBody){
    return this.http.post("/api/award",reqBody);
  }

  // List of Activities performed by student of particular activity
  getStudentPerformedActivities(studentId,actiId){
    return this.http.get("/api/student/"+studentId+"/activities?activityId="+actiId);
  }

  // to get activities of student whose status = REVIEWED
  getStudentActivities(studentCid){
    return this.http.get("/api/student/activities?status=reviewed&studentId=" + studentCid);
  }

  // Get the awards initiated/given by student
  getTeacherAwards(){
    return this.http.get("/api/teacher/awards");
  }

  // to verify the awards by teacher if is_management = TRUE;
  verifyAwards(awardId){
    return this.http.put("/api/teacher/award/"+ awardId +"?Verified=TRUE",{});
  }

  // DOWNLOAD Attachmeents
  downloadAttachment(filePath){
    return this.http.get("/file/download?filePath="+filePath);
  }
}
