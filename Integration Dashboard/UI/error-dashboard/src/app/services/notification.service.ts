import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';

const successConfig = {
    timeOut: 3000,
    progressBar: true,
    tapToDismiss: true
};
const errorConfig = {
    timeOut: 10000,
    autoDismiss: false,
    closeButton: true
};
@Injectable({
    providedIn: 'root'
})
export class NotificationService {

    constructor(private toastr: ToastrService) { }

    showInfo() {
        this.toastr.info('Hello world!', 'Toastr fun!');
    }
    showWarning() {
        this.toastr.warning('Hello world!', 'Toastr fun!');
    }
    showSuccess(message) {
        this.toastr.success(message, 'Success..!!', successConfig);
    }
    showError(message) {
        console.log("Here")
        //   this.toastr.success('Hello world!', 'Toastr fun!');
        this.toastr.error(message, 'Error', errorConfig);
    }
}
