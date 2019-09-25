import { Component, OnInit } from '@angular/core';
import {WorkModel} from '../../../model/work.model';
import {WorkService} from '../../../services/work.service';
import {StateService} from '../../../services/state.service';
import {ResponseSingle} from '../../../model/responsesingle.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-query-work',
  templateUrl: './query-work.component.html',
  styleUrls: ['./query-work.component.css']
})
export class QueryWorkComponent implements OnInit {

  formSubmitted = false;
  work: WorkModel;

  constructor(private workService: WorkService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryWork(id: number) {

    this.formSubmitted = false;

    this.workService.getWork(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.work = (responseData as ResponseSingle).dto as WorkModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.workService.setWork(this.work);
        },
        error => {
          console.log(error);
        }
      );
  }

}
