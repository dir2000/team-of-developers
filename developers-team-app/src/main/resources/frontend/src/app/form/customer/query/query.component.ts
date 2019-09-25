import { Component, OnInit } from '@angular/core';
import {CustomerModel} from '../../../model/customer.model';
import {CustomerService} from '../../../services/customer.service';
import {StateService} from '../../../services/state.service';
import {ResponseSingle} from '../../../model/responsesingle.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: ['./query.component.css']
})
export class QueryCustomerComponent implements OnInit {

  formSubmitted = false;
  customer: CustomerModel;

  constructor(private customerService: CustomerService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryCustomer(id: number) {

    this.formSubmitted = false;

    this.customerService.getCustomer(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.customer = (responseData as ResponseSingle).dto as CustomerModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.customerService.setCustomer(this.customer);
        },
        error => {
          console.log(error);
        }
      );
  }

}
