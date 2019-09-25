import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {BillModel} from '../../../model/bill.model';
import {BillService} from '../../../services/bill.service';
import {StateService} from '../../../services/state.service';

@Component({
  selector: 'app-create-bill',
  templateUrl: './create-bill.component.html',
  styleUrls: ['./create-bill.component.css']
})
export class CreateBillComponent implements OnInit {
  formSubmitted: boolean;
  bill: BillModel;

  constructor(private billService: BillService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.bill = form.value as BillModel;
    // this.stateService.dropState();
  }

  createBill(form: NgForm) {
    this.dropDataToDefault(form);

    this.billService.createBill(this.bill)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.bill = (responseData as BillModel);
        },
        error => {
          console.log(error);
        }
      );

    this.stateService.dropState();
  }

  updateBill(form: NgForm) {
    this.dropDataToDefault(form);
    this.bill.id = this.billService.bill.id;
    this.billService.updateBill(this.bill)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.bill = (responseData as BillModel);
        },
        error => {
          console.log(error);
        }
      );

    this.stateService.dropState();
  }

}
