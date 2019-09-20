import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {ManagerService} from '../../../services/manager.service';
import {StateService} from '../../../services/state.service';
import {ManagerModel} from '../../../model/manager.model';

@Component({
  selector: 'app-create-manager',
  templateUrl: './create-manager.component.html',
  styleUrls: ['./create-manager.component.css']
})
export class CreateManagerComponent implements OnInit {
  formSubmitted: boolean;
  manager: ManagerModel;

  constructor(private managerService: ManagerService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.manager = form.value as ManagerModel;
    this.stateService.dropState();
  }

  createManager(form: NgForm) {
    this.dropDataToDefault(form);

    this.managerService.createManager(this.manager)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.manager = (responseData as ManagerModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  updateManager(form: NgForm) {
    this.dropDataToDefault(form);
    this.manager.id = this.managerService.manager.id;
    this.managerService.updateManager(this.manager)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.manager = (responseData as ManagerModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  patchManager(form: NgForm) {
    this.dropDataToDefault(form);

    this.managerService.patchManager(this.managerService.manager.id, this.manager.phone)
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
