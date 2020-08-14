import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {

  public loader: BehaviorSubject<any> = new BehaviorSubject<any>(false);

  constructor() { }

  setLoading(display: boolean) {
    this.loader.next(display);
  }
}
