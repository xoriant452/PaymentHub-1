import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { hostName } from '../shared/hostName';

@Injectable({
  providedIn: 'root'
})
export class MongoService {
  serviceUrl = "";
  host: string;
  mongoServiceUrl: string;
  replayUrl: string;
  getMessage: string;
  getSingleMessage: string;
  getDates: string;

  constructor(private http: Http) { 
    this.host = hostName;
    this.mongoServiceUrl = this.host + "errorMessages";
    this.replayUrl = this.host +  "replayFunctionality";
    this.getMessage = this.host + "fetchSinglePaymentMessage";
    this.getSingleMessage = this.host + "singleErrorMessage";
    this.getDates = this.host + "filterDates";
  }
  
  getErrorMessages(pageNo, perPage, date) {
    const reqParams = {
      pageNumber: pageNo,
      fromTimestamp: date,
      pageSize: perPage

    }
    return this.http.get(this.mongoServiceUrl, {params: reqParams});
  }

  getErrorMessage(messageId: number, screenName: string) {
    const fetchData = {
      reportName: screenName,
      errorMessageId: messageId
    }
    return this.http.get(this.getSingleMessage, {params: fetchData});
  }

  replayMessages(messageList) {
    return this.http.put(this.replayUrl,messageList);
  }

  getFilteredDates() {
    return this.http.get(this.getDates);
  }

  getPaymentMessage(offset: number, topicName: string) {
    const details = {
      offsetId: offset,
      topicName: topicName
    };
    return this.http.get(this.getMessage, {params: details});
  }

}