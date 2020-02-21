import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http : CustomHttpService) {}

  uploadStudentDetails(data) {
    const url = "/students/signUp"
    return this.http.post(url, data);
  }

  getProfile(studentId){
    return this.http.get("/api/students/"+studentId);
  }

  getStudentInfo(){
    return this.http.get("/api/info");
  }

  getSchools(url){
    return this.http.get(url);
  }

  getActivity(schoolId){
    return this.http.get("/activitiesOffered/"+schoolId);
  }

  getCoach(schoolId,activityId){
    return this.http.get("/coaches/"+schoolId+"/"+activityId);
  }

  addActivity(url, formData: FormData) {
    return this.http.post(url, formData)
  }
}
