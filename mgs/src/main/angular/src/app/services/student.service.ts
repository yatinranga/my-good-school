import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';


@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http: CustomHttpService) { }

  // Single student signup
  uploadStudentDetails(data) {
    const url = "/student/signUp"
    return this.http.post(url, data);
  }
  
  // Single teacher signup
  uploadTeacherDetails(data) {
    const url = "/teacher/signUp"
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
  getAllAwards(studentCid){
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

  getGradesOfSchool(schoolId){
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
  getActivityAreas(){
    return this.http.get("/filters");
  }

  // // Get all Focus Area
  // getFocusAreas(){
  //   return this.http.get("/focusAreas");
  // }
  
  // // Get all fourS
  // getFourS(){
  //   return this.http.get("/fourS");
  // }

  // // Get PSD Area 
  // getPsdAreas(){
  //   return this.http.get("/psdAreas");
  // }  

  

}