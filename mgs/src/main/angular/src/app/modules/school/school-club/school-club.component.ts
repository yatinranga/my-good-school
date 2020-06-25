import { Component, OnInit } from '@angular/core';
import { SchoolService } from 'src/app/services/school.service';

@Component({
  selector: 'app-school-club',
  templateUrl: './school-club.component.html',
  styleUrls: ['./school-club.component.scss']
})
export class SchoolClubComponent implements OnInit {
  
  col = "col-12";
  adminInfo: any;
  schoolClubs = [];
  copySchoolClubs = [];
  showSupervisor:boolean = false
  showClubDetails:boolean = false
  club_loader:boolean = false;
  club_obj: any; // Used to send Club Obj to Club-details Component
  search:any = ""; //Used for Search in table

  psdAreaArr = [];
  focusAreaArr = [];
  fourSArr = [];
  fourS: any = '';
  psdAreas: any = '';
  focusAreas: any = '';
  clubType: any = '';

  constructor(private schoolService: SchoolService) { }

  ngOnInit() {
    this.adminInfo = JSON.parse(localStorage.getItem('user_info'));
    this.getClubs();
    this.getFilters();
  }

  /** List of All Clubs and Socities offered in School */
  getClubs(){
    this.club_loader = true;
    this.schoolService.getAllClubs(this.adminInfo.schoolId).subscribe((res) => {
    this.club_loader = false;
      console.log(res);
      this.schoolClubs = res;
      this.copySchoolClubs = Object.assign([], res);
    },(err) => {
      console.log(err);
    this.club_loader = false;
    })
  }

  /** List of All Filters -  */
  getFilters(){
    this.schoolService.getActivityAreas().subscribe((res)=> {
      this.psdAreaArr = res["PSD Areas"]
      this.focusAreaArr = res["Focus Areas"]
      this.fourSArr = res["Four S"]
    },(err) => {console.log(err);})
  }

  /** Filter Clubs/Societies on th basis of PSD, 4S and Focus Area */
  filterClubs(){
    this.schoolClubs = this.filter(Object.assign([], this.copySchoolClubs));
  }

  /** Actual Filterting of Awards on the basis of selected criteria */
  filter(array: any[]){
    let filterClubsArr = [];
    if(this.clubType && this.fourS && this.focusAreas){
      // filterClubsArr = array.filter(e => e.clubOrSociety && e.clubOrSociety == this.clubType)
    }
    else if(this.clubType && this.fourS){
      filterClubsArr = array.filter(e => e.fourS && e.fourS == this.fourS && e.clubOrSociety && e.clubOrSociety == this.clubType);
    }
    else if(this.clubType && this.focusAreas){

    }
    else if(this.focusAreas && this.fourS){

    }
    else if(this.clubType){
      filterClubsArr = array.filter(e => e.clubOrSociety && e.clubOrSociety == this.clubType);
    }
    else if(this.focusAreas){
      
    }
    else if(this.fourS){
      filterClubsArr = array.filter(e => e.fourS && e.fourS == this.fourS);
    }
    else{
      filterClubsArr = array;
    }
    return filterClubsArr;
  }

  setShowWindow(type, club_obj? ){
    console.log(club_obj);
    this.club_obj = club_obj;
    if(type == 'supervisor'){
      this.showClubDetails = false;
      this.showSupervisor = true;
      this.showSupervisor? (this.col="col-8") : (this.col = "col-12");
    }

    if(type =='club'){
      this.showClubDetails = true;
      this.showClubDetails? (this.col="col-8") : (this.col = "col-12");
      this.showSupervisor = false;      
    }

    if(type =='No Window'){
      this.showClubDetails = false;
      this.showSupervisor = false;
      this.col = "col-12";
    }
  }

}
