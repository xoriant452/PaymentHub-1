import { FormGroup, FormControl } from '@angular/forms';
import { PAGINATOR_CONST } from './../app.constants';
import { CommonService } from './../services/common-service';
import { RESTService } from '../services/rest.service';
import { Component, OnInit, ViewChild, Inject, TemplateRef } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource, MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild('callAPIDialog', { static: false }) callAPIDialog: TemplateRef<any>;

  errDataCols: string[] = ['applicationName', 'customerName', 'transactionName', 'sourceSystem', 'targetSystem', 'fileUploaded', 'errorRecord', 'userName', 'timestamp', 'total', 'success', 'failed'];
  footerColumns = ['noData'];
  errData: any = {};
  PAGINATOR_CONST: any = PAGINATOR_CONST;
  searchPanelOpenState: boolean;
  errorRecordDetails: any = {};
  constructor(private restSerivice: RESTService, public commonService: CommonService, private router: Router, @Inject(DOCUMENT) private document: any, private dialog: MatDialog) { }
  searchForm = new FormGroup({
    fromDate: new FormControl(''),
    toDate: new FormControl(''),
    applicationName: new FormControl(''),
    transactionName: new FormControl(''),
    status: new FormControl('')
  });
  ngOnInit() {
    // this.errLogData.paginator = this.paginator;
    this.resetData();
    //this.getErrorData(0, this.PAGINATOR_CONST.defaultPageSize);
  }

  ngAfterViewInit() {
    this.paginator.page.subscribe(
      (event: any) => {
        this.getErrorData(event.pageIndex, event.pageSize);
      }
    )
  };


  getErrorData(page: number, size: number) {
    //http://localhost:8089/getPaginatedErrorList?page=1&size=12&sortBy=applicationName&order=0
    let apiPath = `/getPaginatedErrorList?page=${page}&size=${size}&sortBy=lastUpdated&order=1`;
    //https://54.158.116.133:8089/getPaginatedErrorList?page=0&size=12&sortBy=lastUpdated&order=1
    //check if search functionality is activated
    apiPath = this.prepareSearchUrl(apiPath);
    this.restSerivice.getData(apiPath).subscribe((data) => {
      this.errData = data;
      let resData: any = [];
      resData = this.errData.dashboardInfo ? this.errData.dashboardInfo : [];
      this.errData.dashboardInfo = new MatTableDataSource(resData);
    }, (error) => {
      console.log(error)
    });
  }

  toggleSearch() {
    this.searchPanelOpenState = !this.searchPanelOpenState;
  }
  cancelSearch() {
    this.searchPanelOpenState = false;
    this.searchForm.reset();
  }
  search() {
    this.getErrorData(0, this.PAGINATOR_CONST.defaultPageSize);
  }
  resetData() {
    this.searchForm.reset();
    this.getErrorData(0, this.PAGINATOR_CONST.defaultPageSize);
  }

  prepareSearchUrl(apiPath: string) {

    //Check if all values are null means form is reset.
    let allNull = true;
    for (var key in this.searchForm.value) {
      if (this.searchForm.value[key] != null || this.searchForm.value[key] != "") {
        allNull = false;
      }
    }
    if (allNull) {
      return apiPath;
    }

    //Prepare search query:
    //Check From Date:
    if (this.searchForm.value.fromDate) {
      apiPath = apiPath + `&fromDate=${this.searchForm.value.fromDate}`;
    }
    //To Date
    if (this.searchForm.value.toDate) {
      apiPath = apiPath + `&toDate=${this.searchForm.value.toDate}`;
    }
    //App Name
    if (this.searchForm.value.applicationName) {
      apiPath = apiPath + `&applicationName=${this.searchForm.value.applicationName}`;
    }
    //Trabsaction
    if (this.searchForm.value.transactionName) {
      apiPath = apiPath + `&transactionName=${this.searchForm.value.transactionName}`;
    }

    //Status
    // if (this.searchForm.value.status) {
    //   apiPath = apiPath + `&status=${this.searchForm.value.status}`;
    // }

    return apiPath;
  }
  exportRecords(type: string) {
    ///exportRecords?applicationName=iX%20by%20Raistone&transactionName=Sales%20Order&fromDate=2020-07-14&toDate=2020-07-18
    let apiPath = `/exportRecords?`;
    if (type == 'filtered') {
      apiPath = this.prepareSearchUrl(apiPath);
    }
    this.restSerivice.exportExcel(apiPath).then((response: any) => {
    }).catch((error: any) => {
      console.log(error)
    });
  }

  viewFile(recordId: String) {
    // uncomment Below 4 lines for open a seperate window to view error file
    // let location = this.document.location;
    // let windowLocation = location.origin + location.pathname + '#/view-error-data?recordId=' + recordId;
    // var winFeature = 'location=no,toolbar=no,menubar=no,scrollbars=yes,resizable=yes';
    // window.open(windowLocation, 'null', winFeature);
    this.viewErrorFile(recordId)
  }

  // viewDataOnWindow(responseData: any) {
  //   var winFeature =
  //     'location=no,toolbar=no,menubar=no,scrollbars=yes,resizable=yes';
  //   window.open('https://www.w3schools.com', 'null', winFeature);
  // }

  viewErrorFile(recordId: String) {
    //fetch error details
    //add path param here later
    let apiPath = `/downloadLogs`;
    // if (type == 'filtered') {
    //   apiPath = this.prepareSearchUrl(apiPath);
    // }
    this.restSerivice.viewRecords(apiPath).then((response: any) => {
      this.errorRecordDetails = response;
      let dialogRef = this.dialog.open(this.callAPIDialog);
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


// <!DOCTYPE html>
// <html>
// <body>

// <p>Click the button to open a new browser window.</p>

// <button onclick="myFunction()">Try it</button>

// <script>
// function myFunction() {
// var winFeature =
//         'location=no,toolbar=no,menubar=no,scrollbars=yes,resizable=yes';
// window.open('https://www.w3schools.com','null',winFeature);  
// }
// </script>

// </body>
// </html>
