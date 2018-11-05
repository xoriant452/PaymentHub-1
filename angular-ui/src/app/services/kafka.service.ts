import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { hostName } from '../shared/hostName';


@Injectable({
  providedIn: 'root'
})
export class KafkaService {
  host: string;
  getMessage: string;
  getTopic: string;
  getDates: string;

  constructor(private http: Http) {
    this.host = hostName;
    this.getMessage = this.host + "paymentMessages";
    this.getTopic = this.host + "topicList";
    this.getDates = this.host + "filterDates";
  }

  getTopics() {
    return this.http.get(this.getTopic);
  }

  getFilteredDates() {
    return this.http.get(this.getDates);
  }
  
  getMessages(topicName: string,message: string, uter: string, pageNum: number, pageSize: number, timeStamp: string) {
    const details = {
      topicName: topicName ? topicName: "",
      msgData: message ? message : "",
      uter: uter ? uter : "",
      pageNumber: pageNum.toString(),
      pageSize: pageSize.toString(),
      msgTimestamp: timeStamp
    }
    return this.http.post(this.getMessage, details);
  }
}