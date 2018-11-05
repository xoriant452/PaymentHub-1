import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { MongoService } from '../../services/mongo.service';
import { ReviewService } from '../../services/review.service';
import { PagerService } from '../../services/Pager.service';
import { screenNames } from '../const';

@Component({
  selector: 'table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css']
})
export class TableComponentComponent implements OnInit {
  @Input() tableContent: any;
  @Input() browserTitle: string;
  @Input() topicName: string;
  @Input() pageSize: number;
  @Output() clickPage: EventEmitter<number> = new EventEmitter<number>();


  contentMessage = [];
  pages = [];
  selectedMessages = [];
  noOfPages: number;
  msgId: number;
  firstRecord: number;
  lastRecord: number;
  pageNo: number;
  selectedAll: any;
  isSelected: boolean;
  isFirstPage: boolean;
  isLastPage: boolean;
  showModal: boolean;
  confirm: boolean;
  showAlert: boolean;
  showSpinner: boolean;
  message: string;
  alertMsg: string;
  paginationMessage: string;
  approveType: string;
  pager = {};
  screenName = {};


  constructor(private mongoService: MongoService, private reviewService: ReviewService,
    private pagerService: PagerService) { }
  ngOnInit() {
    this.screenName = screenNames;
    this.setUpTable();
  }

  setUpTable() {
    this.pages = [];
    this.isSelected = false;
    this.confirm = false;
    this.showAlert = false;
    let contentMsg = [];

    contentMsg = this.tableContent.content;
    if (contentMsg.length > 0) {
      this.contentMessage = contentMsg;
      this.noOfPages = this.tableContent.totalPages;
      this.pageNo = this.tableContent.number + 1;
      this.isFirstPage = (this.pageNo == 1);
      this.isLastPage = (this.pageNo === this.noOfPages);

      // for (let count = 0; count < this.noOfPages; count++) {
      //   this.pages.push(count + 1);
      // }
      this.setPage(this.pageNo);


      this.paginationMessage = `Displaying page ${this.pageNo} of ${this.noOfPages} ${this.noOfPages > 1 ? 'pages' : 'page'}`;
    }
  }

  setPage(page: number) {
    // get pager object from service
    this.pager = this.pagerService.getPager(this.noOfPages, page, this.pageSize);
  }

  showCheckBox() {
    return (this.browserTitle === screenNames.error || this.browserTitle === screenNames.approve);
  }
  isReview() {
    return (this.browserTitle === screenNames.history || this.browserTitle === screenNames.approve);
  }

  selectAll() {
    for (var i = 0; i < this.contentMessage.length; i++) {
      this.contentMessage[i].selected = this.selectedAll;
    }
    this.isSelected = this.selectedAll;
  }


  select() {
    this.selectedAll = this.contentMessage.every((item: any) => {
      return item.selected == true;
    });
    this.isSelected = this.contentMessage.some((item: any) => {
      return item.selected == true;
    });
  }


  openPaymentMessage(offset: number, topicName: string) {
    this.showSpinner = true;
    this.mongoService.getPaymentMessage(offset, topicName).subscribe(
      (response) => {
        const msg = response.json();
        this.message = msg.message;
        this.msgId = offset;
        this.showSpinner = false;
        this.showModal = true;
      });
  }
  openErrorMessage(msgId: number, offsetId: number) {
    let screenName: string;
    let offId = offsetId;
    !msgId ? msgId = offsetId : '';

    switch (this.browserTitle) {
      case screenNames.error:
        screenName = "MASTER";
        break;
      case screenNames.history:
        screenName = "HISTORY";
        break;
      case screenNames.approve:
        screenName = "TRANS";
        break;
    }

    this.showSpinner = true;
    this.mongoService.getErrorMessage(msgId, screenName).subscribe(
      (response) => {
        const msg = response.json();
        this.message = msg.message;
        this.msgId = offId;
        this.showSpinner = false;
        this.showModal = true;
      });
  }
  closeModal(e) {
    this.showModal = false;
  }
  pageClick(no: number) {
    //this.setPage(no);
    no != this.pageNo ? this.clickPage.emit(no) : "";
  }
  setSelectedMessages() {
    if (this.isReview()) {
      this.contentMessage.map((item: any) => {
        item.selected == true ? this.selectedMessages.push(item.id) : '';
      });
    } else {
      this.contentMessage.map((item: any) => {
        item.selected == true ? this.selectedMessages.push(item.offset) : '';
      });
    }
  }
  refreshContent(status: string = "") {
    if (this.isReview()) {
      this.alertMsg = `${(this.selectedMessages.length > 1) ? 'Messages have' : 'Message has'} been ${(status === 'Approve') ? 'Approved' : 'Rejected'}`;
      this.showAlert = true;
      this.contentMessage = this.contentMessage.filter((item: any) => {
        return !this.selectedMessages.includes(item.id);
      });
    } else {
      this.alertMsg = `${(this.selectedMessages.length > 1) ? 'Messages have' : 'Message has'} been sent for approval`;
      this.showAlert = true;
      this.contentMessage.map((item: any) => {
        if (this.selectedMessages.includes(item.offset)) {
          item.isProcessed = true;
          item.selected = false;
        }
      });
    }
  }
  replayMessages() {
    this.setSelectedMessages();
    this.mongoService.replayMessages(this.selectedMessages)
      .subscribe(
        (response) => {
          this.refreshContent();
        },
        (error) => console.log(error)
      );
  }

  confirmAction(status: string) {
    this.setSelectedMessages();
    this.approveType = status;
    this.confirm = true;
  }


  reviewMessages(status: string) {
    this.confirm = false;
    let approveType = (status === 'Approve') ? 'A' : 'R'
    this.reviewService.reviewMessages(this.selectedMessages, approveType)
      .subscribe(
        (response) => {
          this.refreshContent(status);
        },
        (error) => console.log(error)
      );

  }
  closeConfirm() {
    this.confirm = false;
  }
  closeAlert() {
    this.showAlert = false;
  }
}
