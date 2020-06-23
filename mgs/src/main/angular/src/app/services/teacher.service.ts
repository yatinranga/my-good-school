import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

  constructor(private http: CustomHttpService) { }

  // Single teacher signup
  uploadTeacherDetails(data) {
    const url = "/teacher/signUp"
    return this.http.post(url, data);
  }

  // to get the list of all schools
  getSchools(url) {
    return this.http.get(url);
  }

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
  getAllActivity(coachId) {
    return this.http.get("/api/coach/activities?coachId=" + coachId);
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
    return this.http.get("/activities");
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
  assignAward(reqBody) {
    return this.http.post("/api/award", reqBody);

  }

  // List of Activities performed by student of particular activity
  getStudentPerformedActivities(awardCriterion, criterionValue, startDate, endDate, gradeId?) {
    if (gradeId) {
      return this.http.get("/api/award/students?awardCriterion=" + awardCriterion + "&criterionValue=" + criterionValue + "&startDate=" + startDate + "&endDate=" + endDate + "&gradeId=" + gradeId);
    }
    else {
      return this.http.get("/api/award/students?awardCriterion=" + awardCriterion + "&criterionValue=" + criterionValue + "&startDate=" + startDate + "&endDate=" + endDate);
    }
  }

  // to get activities of student whose status = REVIEWED
  getStudentActivities(studentCid) {
    return this.http.get("/api/student/activities?status=reviewed&studentId=" + studentCid);
  }

  // Get the awards initiated/given by student
  getTeacherAwards() {
    return this.http.get("/api/teacher/awards");
  }

  // to verify the awards by teacher if is_management = TRUE;
  verifyAwards(awardId) {
    return this.http.put("/api/teacher/award/" + awardId + "?Verified=TRUE", {});
  }

  // DOWNLOAD Attachmeents
  downloadAttachment(filePath) {
    return this.http.get("/file/download?filePath=" + filePath);
  }

  // Profile Photo Update
  putProfilePhoto(formData) {
    return this.http.put("/api/teacher/profilePic", formData);
  }

  // Update Contact Details
  updateProfile(teacherId, form) {
    return this.http.put("/api/teacher/update/" + teacherId, form);
  }

  // to Award Criteria
  getAwardCriteria() {
    return this.http.get("/awardCriteria");
  }

  // get Values of PSD, Focus Area & 4s
  getAwardCriteriaValue() {
    return this.http.get("/filters");
  }

  // get all club and society request
  getClubReq() {
    return this.http.get("/api/teacher/club/members");
  }

  // Approve or reject club Request
  approveClubReq(stuId, actId, verify) {
    return this.http.put("/api/teacher/club?studentId=" + stuId + "&activityId=" + actId + "&verified=" + verify, {});
  }

  // get all clubs and society under teacher
  getAssignedClubs() {
    return this.http.get("/api/teacher/clubs");
  }

  // Create new Session
  createNewSession(sessionForm) {
    return this.http.post("/api/session", sessionForm);
  }


  // get Student Session Schedule
  getSession(duration) {
    return this.http.get("/api/teacher/sessions?sessionFetch=" + duration);
  }

  // edit Session Schedule
  updateSession(sessionForm) {
    return this.http.post("/api/session/update", sessionForm);
  }

  // delete Session Schedule
  deleteSession(sessionId) {
    return this.http.delete("/api/session/" + sessionId);
  }

  // get Session Schedule for a particular club or society
  getSupervisedClubSession(clubId, duration?) {
    // return this.http.get("/api/teacher/sessions/club/"+clubId+"?sessionFetch="+duration)
    if (duration)
      return this.http.get("/api/teacher/sessions/club/" + clubId + "?sessionFetch=" + duration);
    else
      return this.http.get("/api/teacher/sessions/club/" + clubId);
  }

  // get List of Students enrolled in a particular Club/Society under specific Supervisor and grade
  getSupervisorClubStudents(activityId, teacherId) {
    return this.http.get("/api/students/activity?activityId=" + activityId + "&teacherId=" + teacherId);
  }

  getSupervisorClubReq(clubId) {
    return this.http.get("/api/teacher/club/" + clubId + "/members")
  }

  /** Coordinator APIs */

  // Get Student of Coordinator
  getCoordinatorStudents(id) {
    return this.http.get("/api/school/" + id + "/students");
  }

  // // Get Performed Activity by student of Coordinator
  // getCoordinatorPerfActi(){
  //   return this.http.get("api/student/activities");
  // }

}
