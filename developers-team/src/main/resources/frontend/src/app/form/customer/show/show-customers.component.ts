import {Component, OnInit} from '@angular/core';
import {CustomerModel} from '../../../model/customer.model';
import {ResponseArray} from '../../../model/responsearray.model';
import {StateService} from '../../../services/state.service';
import {CustomerService} from '../../../services/customer.service';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-customer',
  templateUrl: './show-customers.component.html',
  styleUrls: ['./show-customers.component.css']
})
export class ShowCustomersComponent implements OnInit {

  customers: CustomerModel[];

  constructor(private customerService: CustomerService, private stateService: StateService) {
     this.customers = [];
  }

  ngOnInit() {

    const statusUpdate = this.stateService.stateObservable;

    statusUpdate.subscribe(x => {
      if (x === this.stateService.getStates().UNDEFINED) {
        this.getCustomer();
      }
    });

  }

  getCustomer() {
    const response = this.customerService.getCustomers();

    response.subscribe(responseData => {
        this.customers = ((responseData as ResponseArray).response as CustomerModel[]);
        console.log(this.customers);
      },
      error => {
        console.log(error);
      }
    );
  }

  createCustomer() {
    this.stateService.setState(StateEnum.CREATE);
    this.customerService.customer = new CustomerModel();
  }

  updateCustomer(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.customerService.setCustomer(this.customers.find(b => b.id === id));
  }

  patchAddress(id: number) {
    this.stateService.setState(StateEnum.PATCH);
    this.customerService.setCustomer(this.customers.find(b => b.id === id));
    console.log(this.stateService.state.toString());
  }

  deleteCustomer(id: number) {
    const response =  this.customerService.deleteCustomer(id);

    response.subscribe( responseData => {
        console.log(responseData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
