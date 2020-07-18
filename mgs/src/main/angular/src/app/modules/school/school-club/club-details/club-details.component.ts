import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AlertService } from 'src/app/services/alert.service';
import { ObjectUnsubscribedError } from 'rxjs';

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
  nameChanged: boolean = false; //When Club name is changed

  constructor(private schoolService: SchoolService, private formBuilder: FormBuilder, private alertService: AlertService) { }


  ngOnInit() {
    this.getFocusArea();
    this.createForm();

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

  ngOnChanges(clubObj) {
    this.nameChanged = false;
  }

  createForm() {
    this.editClubForm = this.formBuilder.group({
      id: [],
      name: [],
      description: [],
      fourS: [],
      clubOrSociety: [],
      focusAreaRequests: []
    });
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

    this.onClubNameChanges();
  }

  updateClub() {
    // this.alertService.showLoader("");
    const arr = [];
    Object.keys(this.focusareaIds).forEach(key => {
      if (this.focusareaIds[key]) {
        arr.push({ id: key })
      }
    })
    this.editClubForm.value.focusAreaRequests = arr;

    // If name is not changes, then remove the name field
    const clubReq = {};
    Object.keys(this.editClubForm.value).forEach(key => {
      if (!this.nameChanged) {
        if (!(key == "name")) {
          if (this.editClubForm.get(key).value !== null)
            clubReq[key] = this.editClubForm.get(key).value;
        }
      }
      else {
        if (this.editClubForm.get(key).value !== null)
          clubReq[key] = this.editClubForm.get(key).value;
      }
    });
    clubReq['focusAreaRequests'] = arr;
    this.schoolService.updateClub(clubReq).subscribe(res => {
      this.updatedClub.emit("Update Table");
      this.clubObj = res;
      $('#editClubModal').modal('hide');
      this.alertService.showMessageWithSym('Club Updated !', 'Updated', 'success');
      this.createForm();
    }, (err => {
      console.log(err);
      this.errorMessage(err);
    }));
  }

  resetForm() {
    this.editClubForm.reset();
    this.focusareaIds = {};
    this.nameChanged = false;
    this.createForm();
  }

  onClubNameChanges() {
    this.editClubForm.get("name").valueChanges.subscribe(x => {
      this.nameChanged = true;
    })
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
