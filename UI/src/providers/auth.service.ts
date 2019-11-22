import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {  Headers } from '@angular/http'; 
import 'rxjs/add/operator/map';
import { Storage } from '@ionic/storage';
import { HttpParams } from '@angular/common/http';



@Injectable()
export class Auth {

  public token: any;
  public data: any;
  public sessionId: any;

  constructor(public http: HttpClient, public storage: Storage) {

  }

  checkAuthentication() {

    console.log("entering the authentication")

    return new Promise((resolve, reject) => {

      //Load token if exists
      this.storage.get('token').then((value) => {

        this.token = value;
        console.log("this.token" + this.token)

        let headers = new Headers();
        headers.append('Access-Control-Allow-Origin', '*');
        headers.append('Access-Control-Allow-Credentials', 'true');
        headers.append('Authorization', this.token);

        this.http.get('http://localhost:8181/machi/user/login?')
          .subscribe(res => {
            resolve(res);
          }, (err) => {
            reject(err);
          });

      });

    });

  }

  createAccount(details) {
    console.log(details.user)

    return new Promise((resolve, reject) => {

      let headers = new Headers();
      headers.append('Access-Control-Allow-Origin', '*');
      headers.append('Content-Type', 'application/json');

      this.http.get('http://localhost:8181/machi/user/login')
        .subscribe(res => {

         

        }, (err) => {
          reject(err);
        });

    });

  }

  login(credentials) {

    return new Promise((resolve, reject) => {

      

      let headers = new Headers();
      headers.append('Access-Control-Allow-Origin', '*');
      
      headers.append('Access-Control-Allow-Credentials', 'true');
      

      let params = new HttpParams();
      params = params.append('loginId', credentials.user);
      params = params.append('password', credentials.password);

    
      this.http.get('http://localhost:8181/machi/user/login', { params: params })
        .subscribe(res => {
       
          this.data = res;
          this.sessionId = this.data.sessionId
         
          this.token = res;
          this.storage.set('token', "1233444");
          resolve(this.data);
        }, (err) => {
          reject(err);
        });

    });

  }

  logout() {
    this.storage.set('token', '');
  }
}