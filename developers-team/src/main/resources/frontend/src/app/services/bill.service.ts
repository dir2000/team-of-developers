import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {BillModel} from '../model/bill.model';

@Injectable({
  providedIn: 'root'
})
export class BillService {

  path: string;
  bill: BillModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/bills';
  }

  setBill(bill: BillModel) {
    this.bill = bill;
  }

  getBills() {

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

  getBill(id: number) {

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

  createBill(bill: BillModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        bill,
        options
      );
  }

  updateBill(bill: BillModel) {

    console.log("bill update");
    console.log(bill);
    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        bill,
        options
      );
  }

  deleteBill(id: number) {

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

