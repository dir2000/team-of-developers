import {Component, OnInit} from '@angular/core';
import {ProjectModel} from '../../../model/project.model';
import {ProjectService} from '../../../services/project.service';
import {StateService} from '../../../services/state.service';
import {ResponseArray} from '../../../model/responsearray.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-project',
  templateUrl: './show-projects.component.html',
  styleUrls: ['./show-projects.component.css']
})
export class ShowProjectsComponent implements OnInit {
  formSubmitted: boolean;
  projects: ProjectModel[];

  constructor(private projectService: ProjectService, private stateService: StateService) {
    this.projects = [];
  }

  ngOnInit() {

    const response = this.projectService.getProjects();

    response.subscribe(responseData => {
        this.projects = ((responseData as ResponseArray).response as ProjectModel[]);
        console.log(this.projects);
      },
      error => {
        console.log(error);
      }
    );

  }

  createProject() {
    this.stateService.setState(StateEnum.CREATE);
    this.projectService.project = new ProjectModel();
  }

  updateProject(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.projectService.setProject(this.projects.find(b => b.id === id));
  }

  patchAddress(id: number) {
    this.stateService.setState(StateEnum.PATCH);
    this.projectService.setProject(this.projects.find(b => b.id === id));
    console.log(this.stateService.state.toString());
  }

  deleteProject(id: number) {
    const response =  this.projectService.deleteProject(id);

    response.subscribe( responseData => {
        console.log(responseData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
