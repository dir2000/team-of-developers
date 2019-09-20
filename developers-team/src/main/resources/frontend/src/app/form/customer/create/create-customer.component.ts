import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {CustomerModel} from '../../../model/customer.model';
import {StateService} from '../../../services/state.service';
import {CustomerService} from '../../../services/customer.service';

@Component({
  selector: 'app-create-customer',
  templateUrl: './create-customer.component.html',
  styleUrls: ['./create-customer.component.css']
})
export class CreateCustomerComponent implements OnInit {
  formSubmitted: boolean;
  customer: CustomerModel;

  constructor(private customerService: CustomerService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.customer = form.value as CustomerModel;
//    this.stateService.dropState();
  }

  createCustomer(form: NgForm) {
    this.dropDataToDefault(form);

    this.customerService.createCustomer(this.customer)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.customer = (responseData as CustomerModel);
        },
        error => {
          console.log(error);
        }
      );

    this.stateService.dropState();
  }

  updateCustomer(form: NgForm) {
    this.dropDataToDefault(form);
    this.customer.id = this.customerService.customer.id;
    this.customerService.updateCustomer(this.customer)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.customer = (responseData as CustomerModel);
        },
        error => {
          console.log(error);
        }
      );

    this.stateService.dropState();
  }

  patchCustomer(form: NgForm) {
    this.dropDataToDefault(form);

    this.customerService.patchCustomer(this.customerService.customer.id, this.customer.address)
      .subscribe(responseData => {
          this.formSubmitted = true;
          console.log(responseData);
        },
        error => {
          console.log(error);
        }
      );

    this.stateService.dropState();
  }
}
