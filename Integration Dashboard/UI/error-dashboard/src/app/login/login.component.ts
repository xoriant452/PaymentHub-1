import { AuthService } from './../services/auth.service';
import { Component, OnInit } from '@angular/core';
import { Validators } from '@angular/forms';
import { FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  returnUrl: String = '/home';
  loginForm = new FormGroup({
    userName: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(public router: Router, private authService: AuthService, private route: ActivatedRoute) {
    if (this.authService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.loginForm.reset();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }
  onSubmit() {

    let postData: any = {};
    postData.apiPath = '/authenticate';
    postData.loginData = {};
    postData.loginData.username = this.loginForm.value.userName;
    postData.loginData.password = this.loginForm.value.password;
    this.authService.authenticate(postData).subscribe((data: any) => {
      this.router.navigate([this.returnUrl]);
      //save token and redirect
      // if (data && data.token) {
      //   this.authService.saveToken(data.token);

      // }
    }, (error) => {
      console.log(error)
      if (error.status === 401) {
        this.loginForm.reset();
      }
    });
    //this.router.navigate(['./home']);
  }

}
