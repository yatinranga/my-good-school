import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable
  ({
    providedIn: 'root'
  })

export class AdminService {

  constructor(public http: CustomHttpService) { }

  UploadExcel(url, formData: FormData) {
    return this.http.post(url, formData)
  }

  getSchools(url) {
    return this.http.get(url);
  }

  getGrades(schoolId) {
    return this.http.get("/grades?schoolId=" + schoolId);
  }

  getActivities(schoolId) {
    return this.http.get("/activitiesOffered/" + schoolId);
  }

  getTemplate(url) {
    return this.http.get(url);
  }

  /** Get List of all the Users */
  getUsers(){
    return this.http.get("/api/users");
  }

  updateUser(id,requestBody){
    return this.http.post("/api/user/"+id,requestBody);
  }

  /** Create New Role */
  createRole(RequestBody){
    return this.http.post("/api/role",RequestBody)
  }

  /** Get List of all the Roles */
  getRoles(){
    return this.http.get("/api/roles");
  }
  /** Get List of all the Authorities */
  getAuthorities(){
    return this.http.get("/api/authorities");
  }
}