import { AuthService } from './services/auth.service';
import { LoadingService } from './services/loader.service';
import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { Router, RouterEvent, NavigationEnd } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  loader: boolean;
  title = 'error-dashboard';
  typesOfShoes: string[] = ['Boots', 'Clogs', 'Loafers', 'Moccasins', 'Sneakers'];
  showNavBar: boolean = true;
  constructor(public location: Location, public router: Router, private loadingService: LoadingService, private spinner: NgxSpinnerService, private authService: AuthService) {

    router.events.subscribe((event: RouterEvent) => {
      if (event instanceof NavigationEnd) {
        if (location.path() === '/login') {
          this.showNavBar = false;
        }
        else {
          //this.showNavBar = true;
          this.showNavBar = false;
        }
      }
    });
  }
  ngOnInit() {
    this.subscribeLoader();
  }
  subscribeLoader() {
    this.loadingService.loader.subscribe((display) => {
      this.loader = display;
      if (display) {
        this.spinner.show();
      } else {
        this.spinner.hide();
      }
    });
  }

  navigateTo(path: string) {

  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
