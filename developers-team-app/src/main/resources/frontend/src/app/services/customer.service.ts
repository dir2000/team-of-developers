import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {CustomerModel} from '../model/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  path: string;
  customer: CustomerModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/customers';
    this.customer = new CustomerModel();
  }

  setCustomer(customer: CustomerModel) {
    this.customer = customer;
  }

  getCustomers() {

    const options: any = {
      headers: {
        Accept: 'application/json'
      }
    };

    return this.http
      .get(
        AppComponent.HOST_NAME + this.path,
        options
      );
  }

  getCustomer(id: number) {

    const options: any = {
      headers: {
        Accept: 'application/json'
      }
    };

    return this.http
      .get(
        AppComponent.HOST_NAME + this.path + '/' + id,
        options
      );
  }

  createCustomer(customer: CustomerModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        customer,
        options
      );
  }

  updateCustomer(customer: CustomerModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        customer,
        options
      );
  }

  patchCustomer(id: number, address: string) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'

      }
    };

    return this.http
      .patch(
        AppComponent.HOST_NAME + this.path + '/' + id + '/address/' + address,
        options
      );
  }

  deleteCustomer(id: number) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .delete(
        AppComponent.HOST_NAME + this.path + '/' + id,
        options
      );
  }

}

