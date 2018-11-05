import { Component, OnInit } from '@angular/core';
import { screenNames } from '../shared/const';

@Component({
  selector: 'app-browser',
  templateUrl: './browser.component.html',
  styleUrls: ['./browser.component.css']
})
export class BrowserComponent implements OnInit {
  screenNames = screenNames;
  title = screenNames.appTitle;

  constructor() { }

  ngOnInit() {
    
  }

}
