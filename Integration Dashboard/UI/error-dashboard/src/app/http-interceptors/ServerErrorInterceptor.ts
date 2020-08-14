import { NotificationService } from './../services/notification.service';
import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpRequest, HttpHandler,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';

@Injectable()
export class ServerErrorInterceptor implements HttpInterceptor {
  constructor(private notificationService: NotificationService) { }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    return next.handle(request).pipe(
      retry(1),
      catchError((error: HttpErrorResponse) => {
        const message = (error.error ? (error.error.message ? error.error.message : error.message) : error.message);
        this.notificationService.showError(message);
        if (error.status === 401) {
          // refresh token
          return throwError(error);
        } else {
          return throwError(error);
        }
      })
    );
  }
}
