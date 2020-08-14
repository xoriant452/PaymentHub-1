import { RESTService } from './../services/rest.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-error-data',
  templateUrl: './view-error-data.component.html',
  styleUrls: ['./view-error-data.component.css']
})
export class ViewErrorDataComponent implements OnInit {

  errorRecordDetails: any = {}
  constructor(private restSerivice: RESTService) { }

  ngOnInit() {
    this.viewError();
    this.resetData();
  }

  resetData() {
    this.errorRecordDetails = {};
  }
  viewError() {
    //add path param here later
    let apiPath = `/downloadLogs`;
    // if (type == 'filtered') {
    //   apiPath = this.prepareSearchUrl(apiPath);
    // }
    this.restSerivice.viewRecords(apiPath).then((response: any) => {
      this.errorRecordDetails = response;
    }).catch((error: any) => {
      console.log(error)
    });
  }

  downloadRecord() {
    let apiPath = `/downloadLogs`;
    this.restSerivice.downloadErrorFile(apiPath).then((response: any) => {
      this.errorRecordDetails = response;
      //this.viewDataOnWindow(response);
    }).catch((error: any) => {
      console.log(error)
    });
  }

}
