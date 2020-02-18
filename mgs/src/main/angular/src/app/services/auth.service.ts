import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { CustomHttpService } from './custom-http-service.service';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: CustomHttpService, private router: Router) { }

  loginUser(user) {
    return this.http.post("/oauth/token?grant_type=password&username=" + user.username + "&password=" + user.password, {});
  }

  loggedIn() {
    return localStorage.getItem('access_token') ? true : false;
  }

  logoutUser() {
    localStorage.removeItem('access_token')
    this.router.navigate(['/login']);
  }

  getInfo(){
    return this.http.get("/api/info");
  }
}
