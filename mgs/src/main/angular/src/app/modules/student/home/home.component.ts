import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private location:Location,private router:Router) { }

  goBack(){ 
    this.location.back();     
  }

  submitActivity(){
    this.router.navigate(["../student/activity"]);
  }

  getAwards(){
    this.router.navigate([]);
  }

  ngOnInit() {
  }

}
