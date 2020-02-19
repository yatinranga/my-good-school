import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  constructor() { }

  ngOnInit() {

    const header = document.getElementById('myDIV');
    const btns = header.getElementsByClassName('nav-item');
    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < btns.length; i++) {
      btns[i].addEventListener('click', function() {
        const current = document.getElementsByClassName('active');
        current[0].className = current[0].className.replace(' active', '');
        this.className += ' active';
      });
    }
  }

}


