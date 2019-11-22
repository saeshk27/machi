

import { HttpClient,HttpParams } from "@angular/common/http";
import { Component, OnInit, ViewChild, ElementRef } from "@angular/core";
import { Chart } from "chart.js";
import { IonicPage, NavController,NavParams} from 'ionic-angular';


@IonicPage()
@Component({
  selector: 'page-admin',
  templateUrl: 'admin.html'
})
export class AdminPage implements OnInit {
  @ViewChild("barCanvas") barCanvas: ElementRef;
  @ViewChild("doughnutCanvas") doughnutCanvas: ElementRef;
  @ViewChild("doughnutCanvasUser") doughnutCanvasUser: ElementRef;
  @ViewChild("lineCanvas") lineCanvas: ElementRef;

  private barChart: Chart;
  private doughnutChart: Chart;
  private lineChart: Chart;

  public name:any;
  public id:any;
  public data:any;
  public currentSprint:any
  public num:any
  public chartData:any[] = [];
  public nameData:any[] = [];
  public totalTicketsData:any[] = [];
  public ticketCountData:any[] = [];

  constructor(public navCtrl: NavController,public navParams: NavParams,public http: HttpClient) {

    
    this.chartData = navParams.get('ratingDataArray');
    this.nameData = navParams.get('nameDataArray');
    this.totalTicketsData = navParams.get('totalTicketArray');
    this.ticketCountData = navParams.get('ticketCountArray');
    this.name = navParams.get('name');
    this.id=navParams.get('id');
}

   
  

  ngOnInit() {
   

    this.doughnutChart = new Chart(this.doughnutCanvas.nativeElement, {
      type: "doughnut",
      data: {
        labels: this.nameData,
        datasets: [
          {
            label: "Score",
            data: this.chartData,
            backgroundColor: [
              "rgba(255, 99, 132, 0.2)",
              "rgba(54, 162, 235, 0.2)",
              "rgba(255, 206, 86, 0.2)",
              "rgba(75, 192, 192, 0.2)",
              "rgba(153, 102, 255, 0.2)"
            ],
            hoverBackgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#FF6384", "#36A2EB"]
          }
        ]
      }
    });

    this.doughnutCanvasUser = new Chart(this.doughnutCanvasUser.nativeElement, {
      type: "doughnut",
      data: {
        labels: ["Completed", "In Progress", "Open", "Blocked"],
        datasets: [
          {
            label: "Score",
            data: this.ticketCountData,
            backgroundColor: [
              "rgba(54, 162, 235, 0.2)",
              "rgba(255, 206, 86, 0.2)",
              "rgba(75, 192, 192, 0.2)",
              "rgba(153, 102, 255, 0.2)"
            ],
            hoverBackgroundColor: ["#36A2EB", "#FFCE56", "#FF6384", "#36A2EB"]
          }
        ]
      }
    });
   
    console.log("value of chardata inside ngonint sprint"+this.chartData)
    this.barChart = new Chart(this.barCanvas.nativeElement, {
      type: "bar",
      data: {
        labels: this.nameData,
        
        datasets: [
          {
            label: "# of Tickets",
            data: this.totalTicketsData,
            backgroundColor: [
              "rgba(255, 99, 132, 0.2)",
              "rgba(54, 162, 235, 0.2)",
              "rgba(255, 206, 86, 0.2)",
              "rgba(75, 192, 192, 0.2)",
              "rgba(153, 102, 255, 0.2)",
              "rgba(255, 159, 64, 0.2)"
            ],
            borderColor: [
              "rgba(255,99,132,1)",
              "rgba(54, 162, 235, 1)",
              "rgba(255, 206, 86, 1)",
              "rgba(75, 192, 192, 1)",
              "rgba(153, 102, 255, 1)",
              "rgba(255, 159, 64, 1)"
            ],
            borderWidth: 1
          }
        ]
      },
      options: {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true
              }
            }
          ]
        }
      }
    });


    this.lineChart = new Chart(this.lineCanvas.nativeElement, {
      type: "line",
      data: {
        labels: ["2018 Q3", "2018 Q4", "2019 Q1", "2019 Q2", "2019 Q3", "2019 Q4"],
        datasets: [
          {
            label: "Score",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "rgba(75,192,192,0.4)",
            borderColor: "rgba(75,192,192,1)",
            borderCapStyle: "butt",
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: "miter",
            pointBorderColor: "rgba(75,192,192,1)",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "rgba(75,192,192,1)",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: [75, 69, 80, 81, 86, 85],
            spanGaps: false
          }
        ]
      }
    });
  }

  ScrumCoach(){

    let params = new HttpParams();
    console.log("entering ScrumCoach")
    console.log("value of log name is "+this.name)
    params = params.append('userID',this.id);
   

    this.http.get('http://127.0.0.1:3009/scrumcoach/'+this.name, { params: params })
    .subscribe(res => {
   
      this.data = res;
      console.log("sent to python voice call")
     
   
    }, (err) => {

      console.log("error is "+err.value);
     
    });

  }
  


}
