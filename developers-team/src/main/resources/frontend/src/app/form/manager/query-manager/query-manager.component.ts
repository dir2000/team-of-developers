import { Component, OnInit } from '@angular/core';
import {ManagerModel} from '../../../model/manager.model';
import {ManagerService} from '../../../services/manager.service';
import {StateService} from '../../../services/state.service';
import {ResponseSingle} from '../../../model/responsesingle.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-query-manager',
  templateUrl: './query-manager.component.html',
  styleUrls: ['./query-manager.component.css']
})
export class QueryManagerComponent implements OnInit {

  formSubmitted = false;
  manager: ManagerModel;

  constructor(private managerService: ManagerService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryManager(id: number) {

    this.formSubmitted = false;

    this.managerService.getManager(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.manager = (responseData as ResponseSingle).dto as ManagerModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.managerService.setManager(this.manager);
        },
        error => {
          console.log(error);
        }
      );
  }

}
