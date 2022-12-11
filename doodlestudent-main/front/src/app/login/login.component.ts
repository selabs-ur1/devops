import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {OAuthService} from 'angular-oauth2-oidc';
import { JwksValidationHandler } from 'angular-oauth2-oidc-jwks';
import { authConfig } from 'src/sso.config';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private oauthService:OAuthService, private router : Router) {
    this.configureSingleSignOn();
   }
  
  ngOnInit(): void {
  }

  configureSingleSignOn(){
    this.oauthService.configure(authConfig);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
    this.oauthService.loadDiscoveryDocumentAndTryLogin();
  }

  login() {
    console.log("loging in")
    this.oauthService.initImplicitFlow();
  }

  logout(){
    console.log("loging out")
    this.oauthService.logOut();
  }

  // get token(){
  //   let claims: any = this.oauthService.getIdentityClaims();
  //   //if(claims)this.router.navigate(['home']);
  //   return claims ? claims : null;
  // }

}
