import { Component } from '@angular/core';
import { NavController, LoadingController } from 'ionic-angular';
import { Auth } from '../../providers/auth.service';
import { HomePage } from '../home/home';
import { SignupPage } from '../signup/signup';



@Component({
  selector: 'login-page',
  templateUrl: 'login.html'
})
export class LoginPage {

    email: string;
    password: string;
    loading: any;
    credentials:any;
    public data: any;
    public sessionId: any;
    public id:any;
    public user:any;
    public projectId:any;
    

    constructor(public navCtrl: NavController, public authService: Auth, public loadingCtrl: LoadingController) {

    }

    ionViewDidLoad() {

        this.showLoader();

        //Check if already authenticated
        this.authService.checkAuthentication().then((res) => {
            console.log("Already authorized");
            this.loading.dismiss();
            this.navCtrl.setRoot(HomePage);
        }, (err) => {
            console.log("Not already authorized");
            this.loading.dismiss();
        });

    }

    login(){

        this.showLoader();
        console.log(this.password)
        console.log(this.email)

    

        this.credentials = {
            user: this.email,
            password: this.password
        };
      
        

        this.authService.login(this.credentials).then((result) => {
            this.data = result;
            this.sessionId = this.data.sessionId
            this.id= this.data.id;
            this.projectId=this.data.projectId;
            this.loading.dismiss();
            console.log("this.projectId"+this.projectId)
            this.navCtrl.push(HomePage,{
                email:this.email,
                id:this.id,
                projectId:this.projectId
               
                });
           
          
        }, (err) => {
            this.loading.dismiss();
            console.log(err);
        });

    }

    launchSignup(){
        this.navCtrl.push(SignupPage);
    }

    showLoader(){

        this.loading = this.loadingCtrl.create({
            content: 'Authenticating...'
        });

        this.loading.present();

    }

}