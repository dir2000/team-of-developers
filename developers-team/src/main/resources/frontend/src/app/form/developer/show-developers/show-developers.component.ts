import {Component, OnInit} from '@angular/core';
import {DeveloperModel} from '../../../model/developer.model';
import {DeveloperService} from '../../../services/developer.service';
import {StateService} from '../../../services/state.service';
import {ResponseArray} from '../../../model/responsearray.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-developer',
  templateUrl: './show-developers.component.html',
  styleUrls: ['./show-developers.component.css']
})
export class ShowDevelopersComponent implements OnInit {
  formSubmitted: boolean;
  developers: DeveloperModel[];

  constructor(private developerService: DeveloperService, private stateService: StateService) {
    this.developers = [];
  }

  ngOnInit() {

    const response = this.developerService.getDevelopers();

    response.subscribe(responseData => {
        this.developers = ((responseData as ResponseArray).response as DeveloperModel[]);
        console.log(this.developers);
      },
      error => {
        console.log(error);
      }
    );

  }

  createDeveloper() {
    this.stateService.setState(StateEnum.CREATE);
    this.developerService.developer = new DeveloperModel();
  }

  updateDeveloper(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.developerService.setDeveloper(this.developers.find(b => b.id === id));
  }

  deleteDeveloper(id: number) {
    const response =  this.developerService.deleteDeveloper(id);

    response.subscribe( responseData => {
        console.log(responseData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
