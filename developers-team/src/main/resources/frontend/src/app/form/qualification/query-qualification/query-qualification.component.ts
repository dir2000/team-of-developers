import { Component, OnInit } from '@angular/core';
import {QualificationModel} from '../../../model/qualification.model';
import {QualificationService} from '../../../services/qualification.service';
import {StateService} from '../../../services/state.service';
import {ResponseSingle} from '../../../model/responsesingle.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-query-qualification',
  templateUrl: './query-qualification.component.html',
  styleUrls: ['./query-qualification.component.css']
})
export class QueryQualificationComponent implements OnInit {

  formSubmitted = false;
  qualification: QualificationModel;

  constructor(private qualificationService: QualificationService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryQualification(id: number) {

    this.formSubmitted = false;

    this.qualificationService.getQualification(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.qualification = (responseData as ResponseSingle).dto as QualificationModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.qualificationService.setQualification(this.qualification);
        },
        error => {
          console.log(error);
        }
      );
  }

}
