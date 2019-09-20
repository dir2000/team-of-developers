import {Component, OnInit} from '@angular/core';
import {BillModel} from '../../../model/bill.model';
import {BillService} from '../../../services/bill.service';
import {StateService} from '../../../services/state.service';
import {ResponseSingle} from "../../../model/responsesingle.model";
import {StateEnum} from "../../../enums/state.enum";

@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: ['./query.component.css']
})
export class QueryBillsComponent implements OnInit {

  formSubmitted = false;
  bill: BillModel;

  constructor(private billService: BillService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryBill(id: number) {

    this.formSubmitted = false;

    this.billService.getBill(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.bill = (responseData as ResponseSingle).dto as BillModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.billService.setBill(this.bill);
        },
        error => {
          console.log(error);
        }
      );
  }

}
