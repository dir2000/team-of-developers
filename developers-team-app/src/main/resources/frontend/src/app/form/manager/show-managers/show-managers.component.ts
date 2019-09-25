import {Component, OnInit} from '@angular/core';
import {ManagerService} from '../../../services/manager.service';
import {StateService} from '../../../services/state.service';
import {ResponseArray} from '../../../model/responsearray.model';
import {ManagerModel} from '../../../model/manager.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-manager',
  templateUrl: './show-managers.component.html',
  styleUrls: ['./show-managers.component.css']
})
export class ShowManagersComponent implements OnInit {
  formSubmitted: boolean;
  managers: ManagerModel[];

  constructor(private managerService: ManagerService, private stateService: StateService) {
    this.managers = [];
  }

  ngOnInit() {

    const response = this.managerService.getManagers();

    response.subscribe(responseData => {
        this.managers = ((responseData as ResponseArray).response as ManagerModel[]);
        console.log(this.managers);
      },
      error => {
        console.log(error);
      }
    );

  }

  createManager() {
    this.stateService.setState(StateEnum.CREATE);
    this.managerService.manager = new ManagerModel();
  }

  updateManager(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.managerService.setManager(this.managers.find(b => b.id === id));
  }

  patchAddress(id: number) {
    this.stateService.setState(StateEnum.PATCH);
    this.managerService.setManager(this.managers.find(b => b.id === id));
    console.log(this.stateService.state.toString());
  }

  deleteManager(id: number) {
    const response =  this.managerService.deleteManager(id);

    response.subscribe( responseData => {
        console.log(responseData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
