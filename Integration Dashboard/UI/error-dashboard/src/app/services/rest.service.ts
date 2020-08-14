import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class RESTService {

  constructor(private httpClient: HttpClient) { }
  API_KEY = 'YOUR_API_KEY';
  public getData(apiPath: String) {
    let apiUrl = environment.BASE_URL + apiPath;
    return this.httpClient.get(apiUrl);
  }

  exportExcel(apiPath: String) {
    let promise = new Promise((resolve, reject) => {
      let apiUrl = environment.BASE_URL + apiPath;
      let request = new XMLHttpRequest();
      request.open('GET', apiUrl, true);
      request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
      request.responseType = 'blob';
      request.onload = function (e) {
        if (this.status === 200) {
          var blob = this.response;
          if (window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveBlob(blob, 'temp.csv');
          }
          else {
            var downloadLink = window.document.createElement('a');
            var contentTypeHeader = request.getResponseHeader("Content-Type");
            var contDispo = request.getResponseHeader('Content-Disposition');
            var contDispoData = contDispo.split(';')[1];
            var reportName = contDispoData.split('=')[1];
            downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
            downloadLink.download = reportName;
            document.body.appendChild(downloadLink);
            downloadLink.click();
            document.body.removeChild(downloadLink);
            resolve();
          }
        }
        else {
          reject(e);
        }
      };
      request.onerror = function (e) {
        reject(e);
      }
      request.send();
    });
    return promise;
  }

  viewRecords(apiPath: String) {
    let promise = new Promise((resolve, reject) => {
      let apiUrl = environment.BASE_URL + apiPath;
      let request = new XMLHttpRequest();
      request.open('POST', apiUrl, true);
      request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
      request.responseType = 'blob';
      request.onload = function (e) {
        if (this.status === 200) {
          var blob = this.response;
          //var textPromise = blob.text();
          var reader = new FileReader();
          reader.onload = function () {
            var textPromise = reader.result;
            var contentTypeHeader = request.getResponseHeader("Content-Type");
            var contDispo = request.getResponseHeader('Content-Disposition');
            var contDispoData = contDispo.split(';')[1];
            var reportName = contDispoData.split('=')[1];
            let resData: any = {};
            resData.data = textPromise;
            resData.header = contentTypeHeader;
            resData.fileName = reportName;
            resolve(resData);
          }
          reader.readAsText(blob);
          //  textPromise.then(function (data: any, error: any) {

          //console.log(e)
          // var contentTypeHeader = request.getResponseHeader("Content-Type");
          // var contDispo = request.getResponseHeader('Content-Disposition');
          // var contDispoData = contDispo.split(';')[1];
          // var reportName = contDispoData.split('=')[1];
          // let resData: any = {};
          // resData.data = data;
          // resData.header = contentTypeHeader;
          // resData.fileName = reportName;
          // resolve(resData);
          // });
        }
        else {
          reject(e);
        }
      };
      request.onerror = function (e) {
        reject(e);
      }
      request.send();
    });
    return promise;
  }

  downloadErrorFile(apiPath) {
    let promise = new Promise((resolve, reject) => {
      let apiUrl = environment.BASE_URL + apiPath;
      let request = new XMLHttpRequest();
      request.open('POST', apiUrl, true);
      request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
      request.responseType = 'blob';
      request.onload = function (e) {
        if (this.status === 200) {
          var blob = this.response;
          if (window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveBlob(blob, 'temp.csv');
          }
          else {
            var downloadLink = window.document.createElement('a');
            var contentTypeHeader = request.getResponseHeader("Content-Type");
            var contDispo = request.getResponseHeader('Content-Disposition');
            var contDispoData = contDispo.split(';')[1];
            var reportName = contDispoData.split('=')[1];
            downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
            downloadLink.download = reportName;
            document.body.appendChild(downloadLink);
            downloadLink.click();
            document.body.removeChild(downloadLink);
            resolve();
          }
        }
        else {
          reject(e);
        }
      };
      request.onerror = function (e) {
        reject(e);
      }
      request.send();
    });
    return promise;
  }

}
