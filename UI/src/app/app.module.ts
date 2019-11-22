import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { LoginPage } from '../pages/login/login';
import { SignupPage } from '../pages/signup/signup';
import { Auth } from '../providers/auth.service';
import { IonicStorageModule } from '@ionic/storage';
import { AuthPage } from '../pages/auth/auth';
import { HttpModule} from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { AdminPage } from '../pages/admin/admin';

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    AuthPage,
    AdminPage,
    LoginPage,
    SignupPage
  ],
  imports: [
    BrowserModule,
    HttpModule,
    HttpClientModule,
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot()
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    AuthPage,
    AdminPage,  
    LoginPage,
    SignupPage
  ],
  providers: [
    StatusBar,
    IonicStorageModule,
    Auth,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}