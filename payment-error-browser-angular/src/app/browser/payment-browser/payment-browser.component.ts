import { Component, OnInit, ViewChild } from '@angular/core';
import { KafkaService } from '../../services/kafka.service';
import { screenNames } from '../../shared/const';

@Component({
  selector: 'payment-browser',
  templateUrl: './payment-browser.component.html',
  styleUrls: ['./payment-browser.component.css']
})
export class PaymentBrowserComponent implements OnInit {
  servers = [];
  topics = [];
  regions = [];
  countries = [];
  dates = [];
  regionsObj = {};
  countryObj = {};
  kafkaContent = {};
  datesObj = {};
  perpage = [];
  isServerSelected: boolean = false;
  isTopicSelected: boolean = false;
  receivedMessages: boolean = false;
  selectedTopic: string;
  title: string;
  portNo: number;
  pageSize: number = 10;
  selectedPageSize: number;
  selectedDate: string;
  fromDate: Date;
  showDropdown: boolean = false;
  showLoader: boolean = false;
  JSObject: Object = Object;
  message: string;
  uter: string;

  constructor(private kafkaService: KafkaService) {
    this.title = screenNames.payment;
  }

  ngOnInit() {
    this.perpage = [10, 20, 30, 40, 50];
    this.getTopics();
    this.getDates();
  }

  onChangeTopic(topicName: string) {
    this.isTopicSelected = true;
    this.selectedTopic = topicName;
    this.showDropdown = !this.showDropdown;
  }
  onChangePerPage(e) {
    this.pageSize = e.target.value;
  }
  onChangeDate(e) {
    this.selectedDate = e.target.value;
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  getDates() {
    this.kafkaService.getFilteredDates()
      .subscribe(
        (response) => {
          this.datesObj = response.json();
          this.dates = Object.keys(this.datesObj);
          this.selectedDate = this.dates[0];
          this.receive();
        },
        (error) => console.log(error)
      );

  }

  getTopics() {
    this.kafkaService.getTopics()
      .subscribe(
        (response) => {
          this.topics = response.json();

        },
        (error) => console.log(error)
      );
  }

  fetch(pageNum: number = 1) {
    this.receivedMessages = false;
    this.showLoader = true;
    let pageSize: number = this.selectedPageSize ? this.selectedPageSize : this.pageSize;
    this.kafkaService.getMessages(this.selectedTopic,this.message, this.uter, pageNum, pageSize, this.selectedDate)
      .subscribe(
        (response) => {
          this.kafkaContent = response.json();
          this.selectedPageSize = this.kafkaContent['pageSize'];
          this.receivedMessages = true;
          this.showLoader = false;
        },
        (error) => console.log(error)
      );

  }

  receive() {
    this.selectedPageSize = 0;
    this.fetch();
  }

  goToPage(pageNum: number) {
    this.fetch(pageNum);
  }
}
