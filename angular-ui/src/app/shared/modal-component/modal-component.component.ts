import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'modal-component',
  templateUrl: './modal-component.component.html',
  styleUrls: ['./modal-component.component.css']
})
export class ModalComponentComponent implements OnInit {

  constructor() { }

  @Input() message:string;
  @Input() messageId:number;
  @Output() 
  closeModal = new EventEmitter<string>();
  

  ngOnInit() {
  }

  close() {
    this.closeModal.emit('Close');
  }



}
