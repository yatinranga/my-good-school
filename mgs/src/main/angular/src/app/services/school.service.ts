import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class SchoolService {

  constructor(private http : CustomHttpService) { }
  
  // Signle School Signup
  schoolSignup(data){
    const url = "/school/signUp"
    return this.http.post(url, data);
  }

  // get GENERAL Activities
  getGeneralActivities(){
    return this.http.get("/generalActivities");
  }
}
