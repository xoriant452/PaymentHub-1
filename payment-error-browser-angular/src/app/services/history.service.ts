import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { hostName } from '../shared/hostName';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  host: string;
  revieweUrl: string;

  constructor(private http: Http) { 
    this.host = hostName;
    this.revieweUrl = this. host + "historyReportData";
  }

  getHistoryMessages(pageNo, perPage) {
    let serviceUrl = this.revieweUrl + `?page=${pageNo}&size=${perPage}&sort=errorMsgId`;
    return this.http.get(serviceUrl);
  }

}
