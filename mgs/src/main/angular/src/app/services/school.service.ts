import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class SchoolService {

  constructor(private http: CustomHttpService) { }

  // Signle School Signup
  schoolSignup(data) {
    const url = "/school/signUp"
    return this.http.post(url, data);
  }

  // Bulk Upload of data
  UploadExcel(url, formData: FormData) {
    return this.http.post(url, formData)
  }

  /** Create new user */
  createUser(requestBody) {
    return this.http.post("/api/user", requestBody);
  }

  /** To get All Users of a School */
  getUsers() {
    return this.http.get("/api/users");
  }

  /** To get All  */
  getRoles() {
    return this.http.get("/api/roles");
  }

  // get GENERAL Activities
  getGeneralActivities() {
    return this.http.get("/generalActivities");
  }

  // to get profile of School
  getProfile(schoolId) {
    return this.http.get("/api/school/" + schoolId);
  }

  // Update Profile Details
  updateProfile(schoolId, form) {
    return this.http.put("/api/school/update/" + schoolId, form);
  }

  /** Get List of All Students */
  getStudents() {
    return this.http.get("/api/students");
  }

  /** Get List of All Staff (Supervisor and Coordinator) */
  getStaff() {
    return this.http.get("/api/teachers");
  }

  /** Get Enrolled Clubs/Societies of particular student */
  getStudentClubs(studentId) {
    return this.http.get("/api/student/clubs?studentId=" + studentId);
  }

  /** Assign Club/Society to Supervisor */
  assignClub(reqBody) {
    return this.http.put("/api/teacher/assignActivity", reqBody);
  }

  /** Get All Activities Offered in a School  */
  getAllClubs(schoolId) {
    return this.http.get("/activities");
  }

  /** Get All Grades of a Achool */
  getAllGrades(schoolId) {
    return this.http.get("/grades?schoolId=" + schoolId);
  }

  /** Get List of Supervisor for particular Club/Society */
  getClubSupervisor(schoolId, actiId) {
    return this.http.get("/api/coaches/" + schoolId + "/" + actiId);
  }

  /** Edit Guardians of a Student */
  editGuardian(id, reqBody) {
    return this.http.put("/api/guardian?id=" + id, reqBody);
  }

  /** Add Guardians of a Student */
  addGuardian(reqBody) {
    return this.http.post("/api/guardian", reqBody);
  }

  /** Update Profile Details of Sudent */
  updateStudentProfile(studentId, form) {
    return this.http.put("/api/student/" + studentId, form);
  }

  /** Update Profile Details of Supervisor */
  updateSupervisorProfile(teacherId, form) {
    return this.http.put("/api/teacher/update/" + teacherId, form);
  }

  /** Get PSD , Focus Area and 4S */
  getActivityAreas() {
    return this.http.get("/filters");
  }

  /** Edit/Update Activity Offered details */
  updateClub(reqBody){
    return this.http.post("/api/activitiesOffered",reqBody);
  }

  getFocusArea(){
    return this.http.get("/focusAreas");
  }

}
