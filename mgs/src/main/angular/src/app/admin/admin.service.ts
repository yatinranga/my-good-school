// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';

import { Injectable } from '@angular/core';  
import { HttpHeaders } from '@angular/common/http';  
import { HttpClient } from '@angular/common/http'    
import { Observable } from 'rxjs';  
import { User } from 'src/app/user';

@Injectable
({
  providedIn: 'root'
})
export class AdminService {

  url = "http://localhost:8083/api/students/importStudents";
  
  constructor(public http: HttpClient) { }

  UploadExcel(formData: FormData) {  
    return this.http.post(this.url, formData) 
  } 


  Upload(){
    return this.http.get(this.url);
  }

  getStudent():Observable<any>{
    return this.http.get("http://localhost:8083/api/students/export/student");
  }
}  
 

