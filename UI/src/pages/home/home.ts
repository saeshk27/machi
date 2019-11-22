import { Component } from '@angular/core';
import { IonicPage, NavController,NavParams} from 'ionic-angular';
import { AdminPage } from '../admin/admin';
import { HttpClient,HttpParams } from "@angular/common/http";


@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  public id:any
  public data: any;
  public email:any;
  public projID:any;
  public sprintId:any;
  public projName:any;
  public currentSprint:any;
  public sprintStartDate:any;
  public sprintEndDate:any;
  public dailyStandupTime:any;
  public backlogGrooming:any;
  public past:any;
  public projectId:any;
  public next:any;
  public grid:any;
  public productOwner:any;
  public chartData:any[] = [];
  public userNameData:any[] = [];
  public tickets:any
  public totalTicketsData:any[] = [];
  public ticketCountData:any[] = [];
  
  constructor(public navCtrl: NavController,public navParams: NavParams,public http: HttpClient) {
    this.email=this.navParams.get('email');
    console.log("value of  user in Home page  new value is  is "+this.email);

    this.id = navParams.get('id');
    this.projectId=navParams.get('projectId');
    console.log("value of  projectId in Home page  new value is  is "+this.projectId);
    let params = new HttpParams();
    console.log("value of log id is "+this.id)
    params = params.append('id', this.projectId);
  
    
   
    
    
    http.get('http://localhost:8181/machi/project/getProjectDetails', { params: params })
        .subscribe(res => {
       
          
          this.data = res;
          console.log("value of Type is  "+this.data.members[0].meetingNotes[0].type)

          this.grid=this.data.members;
          console.log("this.grid"+this.grid)
          
          
          

          this.projID=this.data.id
          this.sprintId=this.data.sprintId
          this.projName=this.data.name
          this.productOwner=this.data.productOwnerName
          this.currentSprint=this.data.currentSprint
          this.sprintStartDate=this.data.sprintStartDate
          this.sprintEndDate=this.data.sprintEndDate
          this.dailyStandupTime=this.data.dailyStandupTime
          this.backlogGrooming=this.data.backlogGrooming
         

          console.log("sent to this.projID" +this.projID)
          console.log("sent to this.sprintId" +this.sprintId)
          console.log("sent to this.backlogGrooming" +this.backlogGrooming)
          console.log("sent to this.past" +this.past)


          
          
         
         
        }, (err) => {
          
          console.log("error for the first get for projectids "+err.value);
          
        });



        let adminparams = new HttpParams();
    
        adminparams = adminparams.append('sprintName', 'Machi Previous Sprint');
   
  

      this.http.get('http://localhost:8181/machi/metrics/getMetricsForSprint', { params: adminparams })
      .subscribe(res => {
        this.data = res;
        this.data.forEach( r => {
          console.log("value of user score is"+r.userScore)
         this.tickets= r.completedTickets+r.wipTickets+r.openTickets+r.blockedTickets

        this.chartData.push(r.userScore);
        this.userNameData.push(r.userName);
        this.totalTicketsData.push(this.tickets);

        if(r.userId == this.id) {
          this.ticketCountData.push(r.completedTickets);
          this.ticketCountData.push(r.wipTickets);
          this.ticketCountData.push(r.openTickets);
          this.ticketCountData.push(r.blockedTickets);
        }

      });
    
      
      }, (err) => {
      
        console.log("error for the first get for projectids "+err.value);
        
      });

      

    
  }

  backloggrooming(){
    let params = new HttpParams();
    console.log("value of log name is "+this.email)

    this.http.get('http://127.0.0.1:3009/backloggrooming/'+this.email, { params: params })
    .subscribe(res => {
   
      this.data = res;
      console.log("sent to python voice call")
     
   
    }, (err) => {

      console.log("error is "+err.value);
     
    });

  }

  sprintplanning(){
    let params = new HttpParams();
    console.log("value of log name is "+this.email)

    this.http.get('http://127.0.0.1:3009/sprintplanning/'+this.email, { params: params })
    .subscribe(res => {
   
      this.data = res;
      console.log("sent to python voice call")
     
   
    }, (err) => {

      console.log("error is "+err.value);
     
    });

  }

 

  performance() {
    console.log("entered admin")
    console.log("value of chartData before navctrl " +this.chartData)
    
    this.navCtrl.push(AdminPage,{
      
      ratingDataArray:this.chartData,
      nameDataArray:this.userNameData,
      totalTicketArray:this.totalTicketsData,
      ticketCountArray:this.ticketCountData,
      name:this.email,
      id:this.id
      
     
      });
   
  }
 

  dailySprint() {

    let params = new HttpParams();
    console.log("value of log name is "+this.email)
    
    params = params.append('userID',this.id);
    params = params.append('name', this.email);
    params = params.append('projID', this.projID);
    params = params.append('sprintId', this.sprintId);


    
  this.http.get('http://127.0.0.1:3009/dailysprint/'+this.email, { params: params })
      .subscribe(res => {
     
        this.data = res;
        console.log("sent to python voice call")
       
     
      }, (err) => {
  
        console.log("error is "+err.value);
       
      });
   
    console.log("value of  user in Home page  new value is  is "+this.email);
  }
}
