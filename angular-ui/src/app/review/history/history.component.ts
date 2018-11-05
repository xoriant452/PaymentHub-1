import { Component, OnInit } from '@angular/core';
import { HistoryService } from '../../services/history.service';
import { screenNames } from '../../shared/const';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  title:string;
  historyContent = {};
  receivedMessages: boolean = false;
  showLoader: boolean = false;

  constructor(private historyService: HistoryService) {
    this.title = screenNames.history;
   }

  ngOnInit() {
    this.getMessages();
  }

  getMessages(no:number = 1) {
    this.receivedMessages = false;
    this.showLoader = true;
    this.historyService.getHistoryMessages(no - 1,10)
    .subscribe(
      (response) => {
        this.historyContent = response.json();
        this.receivedMessages = true;
        this.showLoader = false;
      },
      (error) => console.log(error)
    );
  }

  gotoPage(no: number) {
    this.getMessages(no);
  }
}
