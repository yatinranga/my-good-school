import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';

declare let $: any;

@Component({
  selector: 'app-club-details',
  templateUrl: './club-details.component.html',
  styleUrls: ['./club-details.component.scss']
})
export class ClubDetailsComponent implements OnInit {

  // adminInfo: any;
  @Input() clubObj: any;
  @Output() updatedClub = new EventEmitter<string>()

  focusAreaArr = [];
  // clubSupervisor = [];
  // sup_loader: boolean = false;
  editClubForm: FormGroup;
  focusareaIds = {};

  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder, private alertService: AlertService) { }

 
  ngOnInit() {
    // this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    // this.getSupervisor();
    this.getFocusArea();
    this.editClubForm = this.formBuilder.group({
      id: [],
      name: [],
      description: [],
      fourS: [],
      clubOrSociety: [],
      focusAreaRequests: []
    });

    // window.onscroll = function() {myFunction()};

    // var navbar = document.getElementById("card-club-details-card");
    // var sticky = navbar.offsetTop;
    
    // function myFunction() {
    //   if (window.pageYOffset >= sticky) {
    //     navbar.classList.add("sticky")
    //   } else {
    //     navbar.classList.remove("sticky");
    //   }
    // }




    // onscroll = function() {scrollFunction1()};

    // function scrollFunction1() {
    //   if (document.body.scrollTop > 70 || document.documentElement.scrollTop > 70) {
    //     document.getElementById("card-club-details-card").classList.add("sticky");
    //   } else {
    //     document.getElementById("card-club-details-card").classList.remove("sticky");

    //   }
    // }
    
  }



  getFocusArea() {
    this.schoolService.getFocusArea().subscribe(res => {
      this.focusAreaArr = res;
    }, (err => {
      console.log(err);
    }))
  }

  showEditModal() {
    $('#editClubModal').modal('show');
    this.focusareaIds = {};
    this.editClubForm.patchValue({
      id: this.clubObj.id,
      description: this.clubObj.description,
      name: this.clubObj.name,
      fourS: this.clubObj.fourS,
      clubOrSociety: this.clubObj.clubOrSociety
    });
    this.clubObj.focusAreaResponses.forEach(element => {
      this.focusareaIds[element.id] = true;
    });
  }

  updateClub() {
    this.alertService.showLoader("");
    const arr = [];
    Object.keys(this.focusareaIds).forEach(key => {
      if (this.focusareaIds[key]) {
        arr.push({ id: key })
      }
    })
    this.editClubForm.value.focusAreaRequests = arr;
    console.log(this.editClubForm.value);

    this.schoolService.updateClub(this.editClubForm.value).subscribe(res => {
      console.log(res);
      this.updatedClub.emit("Update Table");
      this.clubObj = res;
      $('#editClubModal').modal('hide');
      this.alertService.showMessageWithSym('Club Updated !', 'Updated', 'success');
    }, (err => {
      console.log(err);
      this.errorMessage(err);
    }))
  }

  resetForm() {
    this.editClubForm.reset();
    this.focusareaIds = {};
  }

  /** Handling Error */
  errorMessage(err) {
    if (err.status == 400) {
      this.alertService.showMessageWithSym(err.msg, "", "info");
    }
    else {
      this.alertService.showMessageWithSym("There is some error in server. \nTry after some time !", "Error", "error");
    }
  }

}
