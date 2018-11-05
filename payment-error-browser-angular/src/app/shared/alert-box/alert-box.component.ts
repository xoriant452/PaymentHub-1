import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-alert-box',
  templateUrl: './alert-box.component.html',
  styleUrls: ['./alert-box.component.css']
})
export class AlertBoxComponent implements OnInit {
  @Input() msg: string;
  @Output() close: EventEmitter<any> = new EventEmitter<any>();

  message: string;

  constructor() { }

  ngOnInit() {

  }

  closeAlert() {
    this.close.emit();
  }
}
