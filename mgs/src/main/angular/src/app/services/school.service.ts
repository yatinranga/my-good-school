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
  getUsers(){
    return this.http.get("/api/users");
  }

  /** To get All  */
  getRoles(){
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
  getStudents(){
    return this.http.get("/api/students");
  }

  /** Get List of All Staff (Supervisor and Coordinator) */
  getStaff(){
    return this.http.get("/api/teachers");                      
  }
}
