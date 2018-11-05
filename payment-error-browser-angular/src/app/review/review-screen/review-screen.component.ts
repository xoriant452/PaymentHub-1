import { Component, OnInit } from '@angular/core';
import { ReviewService } from '../../services/review.service';
import { screenNames } from '../../shared/const';

@Component({
  selector: 'app-review-screen',
  templateUrl: './review-screen.component.html',
  styleUrls: ['./review-screen.component.css']
})
export class ReviewScreenComponent implements OnInit {
  title: string;
  reviewContent = {};
  receivedMessages:boolean = false;
  showLoader: boolean = false;

  constructor(private reviewService: ReviewService) { 
    this.title = screenNames.approve;
  }

  ngOnInit() {
    this.getMessages();
  }

  getMessages(no: number = 1) {
    this.receivedMessages = false;
    this.showLoader = true;
    this.reviewService.getReviewMessages(no - 1,10)
    .subscribe(
      (response) => {
        this.reviewContent = response.json();
        this.receivedMessages = true;
        this.showLoader = false;
      },
      (error) => console.log(error)
    );
  }


}
