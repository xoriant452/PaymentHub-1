import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { hostName } from '../shared/hostName';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  serviceUrl = "";
  host: string;
  revieweUrl = "pendingApprovalErrorMessages";
  reviewUrl = "reviewPendingRecords";
  reviewJson = {};
  constructor(private http: Http) { 
    this.host = hostName;
    this.revieweUrl = this.host + this.revieweUrl;
    this.reviewUrl = this.host + this.reviewUrl;
  }
  
  getReviewMessages(pageNo, perPage) {
    this.serviceUrl = this.revieweUrl + `?page=${pageNo}&size=${perPage}&sort=offsetId`;
    return this.http.get(this.serviceUrl);
  }

  reviewMessages(messageList, status) {
    this.reviewJson = {
      "list": messageList,
      "status": status
    }
    return this.http.put(this.reviewUrl,this.reviewJson);
  }
}