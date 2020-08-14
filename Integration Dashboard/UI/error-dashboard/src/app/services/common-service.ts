import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  public snackbar: BehaviorSubject<any> = new BehaviorSubject<any>(false);

  constructor() { }

  showSnackBar(display: boolean) {
    this.snackbar.next(display);
  }
}
