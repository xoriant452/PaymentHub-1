import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    userName: new FormControl(''),
    password: new FormControl('', Validators.required),
  });
  constructor(public router: Router) { }

  ngOnInit() {
  }
  onSubmit() {
    // TODO: Use EventEmitter with form value
    console.warn(this.loginForm.value);
    this.router.navigate(['./home']);
  }

}
