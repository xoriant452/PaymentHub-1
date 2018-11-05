import { Component, OnInit } from '@angular/core';
import { screenNames } from '../shared/const';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {
      screenNames = screenNames;
    title = screenNames.appTitle;

  constructor() { }

  ngOnInit() {

  }

}
