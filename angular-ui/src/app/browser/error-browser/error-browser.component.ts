import { Component, OnInit } from '@angular/core';
import { MongoService } from '../../services/mongo.service';
import { screenNames } from '../../shared/const';

@Component({
  selector: 'error-browser',
  templateUrl: './error-browser.component.html',
  styleUrls: ['./error-browser.component.css']
})
export class ErrorBrowserComponent implements OnInit {
  errorContent = {};
  receivedMessages: boolean = false;
  title: string;
  showLoader: boolean = false;
  datesObj = {};
  dates = [];
  selectedDate: string;
  perpage: number[];
  selectedPageSize: number;
  pageSize: number = 10;




  constructor(private mongoService: MongoService) {
    this.title = screenNames.error;
  }

  ngOnInit() {
    this.perpage = [10, 20, 30, 40, 50];
    this.getDates();
  }
  getMessages(no: number = 1) {
    this.showLoader = true;
    this.receivedMessages = false;
    let pageSize = this.selectedPageSize ? this.selectedPageSize : this.pageSize;
    this.mongoService.getErrorMessages(no,pageSize, this.selectedDate)
    .subscribe(
      (response) => {
        this.errorContent = response.json();
        this.selectedPageSize = this.errorContent['pageSize'];
        this.receivedMessages = true;
        this.showLoader = false;
      },
      (error) => console.log(error)
    );
  }

  receive() {
    this.selectedPageSize = 0;
    this.getMessages();
  }
  getDates() {
    this.mongoService.getFilteredDates()
      .subscribe(
        (response) => {
          this.datesObj = response.json();
          this.dates = Object.keys(this.datesObj);
          this.selectedDate = this.dates[0];
          this.getMessages();
        },
        (error) => console.log(error)
      );

  }
  onChangeDate(e) {
    this.selectedDate = e.target.value;
  }


}
