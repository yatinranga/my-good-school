// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';

import { Injectable } from '@angular/core';  
import { HttpHeaders } from '@angular/common/http';  
import { HttpClient } from '@angular/common/http'    
import { Observable, of } from 'rxjs';  
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

  getSchools(url){
    return this.http.get(url);
  }

  getTemplate(url){
    return this.http.get(url);
  }

}  
 

