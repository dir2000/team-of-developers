import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import {ProjectModel} from '../../../model/project.model';
import {ProjectService} from '../../../services/project.service';
import {StateService} from '../../../services/state.service';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
  formSubmitted: boolean;
  project: ProjectModel;

  constructor(private projectService: ProjectService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.project = form.value as ProjectModel;
    this.stateService.dropState();
  }

  createProject(form: NgForm) {
    this.dropDataToDefault(form);

    this.projectService.createProject(this.project)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.project = (responseData as ProjectModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  updateProject(form: NgForm) {
    this.dropDataToDefault(form);
    this.project.id = this.projectService.project.id;
    this.projectService.updateProject(this.project)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.project = (responseData as ProjectModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  patchProject(form: NgForm) {
    this.dropDataToDefault(form);

    this.projectService.patchProject(this.projectService.project.id, this.project.description)
      .subscribe(responseData => {
          this.formSubmitted = true;
          console.log(responseData);
        },
        error => {
          console.log(error);
        }
      );
  }
}
