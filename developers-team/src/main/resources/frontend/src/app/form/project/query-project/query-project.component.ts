import { Component, OnInit } from '@angular/core';
import {ProjectModel} from "../../../model/project.model";
import {ProjectService} from "../../../services/project.service";
import {StateService} from "../../../services/state.service";
import {ResponseSingle} from "../../../model/responsesingle.model";
import {StateEnum} from "../../../enums/state.enum";

@Component({
  selector: 'app-query-project',
  templateUrl: './query-project.component.html',
  styleUrls: ['./query-project.component.css']
})
export class QueryProjectComponent implements OnInit {

  formSubmitted = false;
  project: ProjectModel;

  constructor(private projectService: ProjectService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryProject(id: number) {

    this.formSubmitted = false;

    this.projectService.getProject(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.project = (responseData as ResponseSingle).dto as ProjectModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.projectService.setProject(this.project);
        },
        error => {
          console.log(error);
        }
      );
  }

}
