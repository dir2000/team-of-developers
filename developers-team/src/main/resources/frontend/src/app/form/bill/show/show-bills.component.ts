import {Component, OnInit} from '@angular/core';
import {BillModel} from '../../../model/bill.model';
import {ResponseArray} from '../../../model/responsearray.model';
import {BillService} from '../../../services/bill.service';
import {StateService} from '../../../services/state.service';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-bill',
  templateUrl: './show-bills.component.html',
  styleUrls: ['./show-bills.component.css']
})
export class ShowBillsComponent implements OnInit {
  bills: BillModel[] = [];

  constructor(private billService: BillService, private stateService: StateService) {
  }

  ngOnInit() {

    const statusUpdate = this.stateService.stateObservable;

    statusUpdate.subscribe(x => {
      if (x === this.stateService.getStates().UNDEFINED) {
        this.getBills();
      }
    });
  }

  getBills() {
    const response = this.billService.getBills();

    response.subscribe(responseData => {
        this.bills = ((responseData as ResponseArray).response as BillModel[]);
        console.log(this.bills);
      },
      error => {
        console.log(error);
      }
    );
  }

  updateBill(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.billService.setBill(this.bills.find(b => b.id === id));
  }

  deleteBill(id: number) {

    this.stateService.setState(StateEnum.DELETE);
    const response =  this.billService.deleteBill(id);

    response.subscribe( responseData => {
      console.log(responseData);
      this.stateService.dropState();
      this.ngOnInit();
      },
      error => {
        console.log(error);
      }
    );
  }
}
