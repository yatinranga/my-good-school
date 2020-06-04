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

  getUsers(){
    return this.http.get("/api/users");
  }
}