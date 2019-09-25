import { Component, OnInit } from '@angular/core';
import {DeveloperModel} from '../../../model/developer.model';
import {DeveloperService} from '../../../services/developer.service';
import {StateService} from '../../../services/state.service';
import {ResponseSingle} from '../../../model/responsesingle.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-query-developer',
  templateUrl: './query-developer.component.html',
  styleUrls: ['./query-developer.component.css']
})
export class QueryDeveloperComponent implements OnInit {

  formSubmitted = false;
  developer: DeveloperModel;

  constructor(private developerService: DeveloperService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryDeveloper(id: number) {

    this.formSubmitted = false;

    this.developerService.getDeveloper(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.developer = (responseData as ResponseSingle).dto as DeveloperModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.developerService.setDeveloper(this.developer);
        },
        error => {
          console.log(error);
        }
      );
  }

}
