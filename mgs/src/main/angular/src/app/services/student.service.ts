import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';
import { HttpClient } from '@angular/common/http';
import { BASE_URL } from './app.constant';



@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http: CustomHttpService, private httpClient: HttpClient) { }

  // Single student signup
  uploadStudentDetails(data) {
    const url = "/student/signUp"
    return this.http.post(url, data);
  }

  // to get profile of student
  getProfile(studentId) {
    return this.http.get("/api/student/" + studentId);
  }

  //  to get info about the student
  getStudentInfo() {
    return this.http.get("/api/info");
  }

  // get ALL AWARDS of student
  getAllAwards(studentCid) {
    return this.http.get("/api/student/awards");
  }

  // to get AWARDS of student by Activity ID
  // getAwards(studentCid,activityId){
  //   return this.http.get("/api/student/awards?studentId=" + studentCid + "&activityId=" + activityId);
  // }

  // to get the list of all schools
  getSchools(url) {
    return this.http.get(url);
  }

  // get GRADES of School
  getGradesOfSchool(schoolId) {
    return this.http.get("/grades?schoolId=" + schoolId);
  }

  // to get all activities in particular school
  getActivity(schoolId) {
    return this.http.get("/activitiesOffered/" + schoolId);
  }

  // to get SAVED activities of student
  getSavedActivity(studentCid) {
    return this.http.get("/api/student/activities?status=saved&studentId=" + studentCid);
  }

  // to get SUBMITTED Activities of student
  getSubmittedActivity(studentCid) {
    return this.http.get("/api/student/activities?status=submitted&studentId=" + studentCid);
  }

  // to get REVIEWED Activities of student
  getReviewedActivity(studentCid) {
    return this.http.get("/api/student/activities?status=reviewed&studentId=" + studentCid);
  }

  // to get ALL Activities of student
  getAllActivity(studentCid) {
    return this.http.get("/api/student/activities?studentId=" + studentCid);
  }

  // to get teacher/coaches who perform particular activity
  getCoach(schoolId, activityId) {
    return this.http.get("/api/coaches/" + schoolId + "/" + activityId);
  }

  // to ADD new activity performed by student
  addActivity(url, formData: FormData) {
    return this.http.post(url, formData)
  }

  // to SUBMIT saved activity by student
  submitActivity(activityPerformedId) {
    return this.http.post("/api/student/" + activityPerformedId + "/submit", {});
  }

  // TO DELELTE saved activity by student
  deleteActivity(activityId) {
    console.log("Delete APi");
    return this.http.delete("/api/student/activity/" + activityId);
  }

  // get PSD , Focus Area and 4S
  getActivityAreas() {
    return this.http.get("/filters");
  }

  // Add Certificate
  addCertificate(formData: FormData) {
    return this.http.post("/api/student/certificate", formData);
  }

  // View Certificates
  getCertificates() {
    return this.http.get("/api/student/certificates");
  }

  // DOWNLOAD Attachmeents
  downloadAttachment(filePath) {
    return this.http.get("/file/download?filePath=" + filePath);
  }

  // Profile Photo Update
  putProfilePhoto(studentCid, formData) {
    return this.http.put("/api/student/" + studentCid + "/profilePic", formData);
  }

  // get List of Students for the Activity
  getActivityStudent(activityId) {
    return this.http.get("/api/students/activity?activityId=" + activityId);
  }

  // get List of Enrolled Clubs and Socities
  getAllEnrolledClub() {
    return this.http.get("/api/student/clubs");
  }

  // Enroll in a new Club or Society
  postEnrollInClub(activityId, supervisorId) {
    return this.http.post("/api/student/club?activityId=" + activityId + "&supervisorId=" + supervisorId, {});
  }

  // get Student Session Schedule
  getSession(duration) {
    return this.http.get("/api/student/sessions?sessionFetch=" + duration);
  }

  // get Session Schedule for a particular club or society
  getEnrolledClubSession(clubId, duration?) {
    if (duration)
      return this.http.get("/api/student/sessions/club/" + clubId + "?sessionFetch=" + duration);
    else
      return this.http.get("/api/student/sessions/club/" + clubId);
  }

  // get Session Schedule of a particular Supervisor for a Club/Society
  getSupervisorSchedule(clubId, teacherId) {
    return this.http.get("/api/student/sessions/club/" + clubId + "?teacherId=" + teacherId);
  }

  // get List of Students enrolled in a particular Club/Society under specific Supervisor and grade
  getSupervisorStudent(activityId, teacherId) {
    return this.http.get("/api/students/activity?activityId=" + activityId + "&teacherId=" + teacherId);
  }

  // get status 
  getMembershipStatus() {
    return this.http.get("api/student/club/membershipStatus");
  }

}