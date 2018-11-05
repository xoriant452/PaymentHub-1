import { RouterModule, Routes } from '@angular/router';

import { PaymentBrowserComponent } from './browser/payment-browser/payment-browser.component';
import { ErrorBrowserComponent } from './browser/error-browser/error-browser.component';
import { ReviewScreenComponent } from './review/review-screen/review-screen.component';
import { ReviewComponent } from './review/review.component';
import { BrowserComponent } from './browser/browser.component';
import { HistoryComponent } from './review/history/history.component';


const appRoutes: Routes = [
    { path: '', redirectTo: 'payments', pathMatch: 'full' },
    { path: 'payments', component: BrowserComponent,
    children: [
        { path: '', redirectTo: 'payment', pathMatch: 'full' },
        { path: 'payment', component: PaymentBrowserComponent }
    ]},

    { path: 'errors', component: ReviewComponent,
      children: [
        { path: '', redirectTo: 'error', pathMatch: 'full' },
        { path: 'error', component: ErrorBrowserComponent },
        { path: 'review', component: ReviewScreenComponent },
        { path: 'history', component: HistoryComponent },
      ]
    }
];

export const routes = RouterModule.forRoot(appRoutes);