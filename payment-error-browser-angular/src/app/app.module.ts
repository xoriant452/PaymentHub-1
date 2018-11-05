import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/Forms';
import { HttpModule } from '@angular/http';

import { routes } from './app.routes';
import { AppComponent } from './app.component';
import { PaymentBrowserComponent } from './browser/payment-browser/payment-browser.component';
import { ErrorBrowserComponent } from './browser/error-browser/error-browser.component';
import { TableComponentComponent } from './shared/table-component/table-component.component';
import { ModalComponentComponent } from './shared/modal-component/modal-component.component';
import { ReviewScreenComponent } from './review/review-screen/review-screen.component';
import { HistoryComponent } from './review/history/history.component';
import { BrowserComponent } from './browser/browser.component';
import { ReviewComponent } from './review/review.component';
import { SearchPipe } from './search.pipe';
import { LoaderComponentComponent } from './shared/loader-component/loader-component.component';
import { ConfirmationBoxComponent } from './shared/confirmation-box/confirmation-box.component';
import { AlertBoxComponent } from './shared/alert-box/alert-box.component';

@NgModule({
  declarations: [
    AppComponent,
    PaymentBrowserComponent,
    ErrorBrowserComponent,
    TableComponentComponent,
    ModalComponentComponent,
    ReviewScreenComponent,
    HistoryComponent,
    BrowserComponent,
    ReviewComponent,
    SearchPipe,
    LoaderComponentComponent,
    ConfirmationBoxComponent,
    AlertBoxComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    routes,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
