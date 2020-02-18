import { Injectable } from '@angular/core';
import { CustomHttpService } from './custom-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http : CustomHttpService) {}

  uploadStudentDetails(data) {
    const url = "/api/student/signUp"
    return this.http.post(url, data);
  }

  getActivity(){    
  }
}
