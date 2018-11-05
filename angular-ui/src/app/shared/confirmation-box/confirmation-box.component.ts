import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-confirmation-box',
  templateUrl: './confirmation-box.component.html',
  styleUrls: ['./confirmation-box.component.css']
})
export class ConfirmationBoxComponent implements OnInit {
  @Input() approveType: string;
  @Input() count: number;
  @Output() confirm: EventEmitter<string> = new EventEmitter<string>();
  @Output() cancel: EventEmitter<any> = new EventEmitter<any>();

  message: string;

  constructor() { }

  ngOnInit() {
    this.message = `Are you sure you want to ${this.approveType} the selected ${this.count > 1 ? 'messages' : 'message'}`;
  }

  confirmAction() {
    this.confirm.emit(this.approveType);
  }

  cancelAction() {
    this.cancel.emit();
  }

}
