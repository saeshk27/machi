import { Component } from '@angular/core';
import { NavController, LoadingController } from 'ionic-angular';
import { Auth } from '../../providers/auth.service';
import { HomePage } from '../home/home';

@Component({
  selector: 'signup-page',
  templateUrl: 'signup.html'
})
export class SignupPage {


  email: string;
  password: string;
  loading: any;

  constructor(public navCtrl: NavController, public authService: Auth, public loadingCtrl: LoadingController) {

  }

  register(){

    this.showLoader();

    let details = {
        email: this.email,
        password: this.password,
       
    };

    this.authService.createAccount(details).then((result) => {
      this.loading.dismiss();
      console.log(result);
      this.navCtrl.setRoot(HomePage);
    }, (err) => {
        this.loading.dismiss();
    });

  }

  showLoader(){

    this.loading = this.loadingCtrl.create({
      content: 'Authenticating...'
    });

    this.loading.present();

  }

}