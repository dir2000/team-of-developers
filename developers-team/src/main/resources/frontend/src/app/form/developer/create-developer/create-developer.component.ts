import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {DeveloperModel} from '../../../model/developer.model';
import {DeveloperService} from '../../../services/developer.service';
import {StateService} from '../../../services/state.service';

@Component({
  selector: 'app-create-developer',
  templateUrl: './create-developer.component.html',
  styleUrls: ['./create-developer.component.css']
})
export class CreateDeveloperComponent implements OnInit {
  formSubmitted: boolean;
  developer: DeveloperModel;

  constructor(private developerService: DeveloperService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.developer = form.value as DeveloperModel;
    this.stateService.dropState();
  }

  createDeveloper(form: NgForm) {
    this.dropDataToDefault(form);

    this.developerService.createDeveloper(this.developer)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.developer = (responseData as DeveloperModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  updateDeveloper(form: NgForm) {
    this.dropDataToDefault(form);
    this.developer.id = this.developerService.developer.id;
    this.developerService.updateDeveloper(this.developer)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.developer = (responseData as DeveloperModel);
        },
        error => {
          console.log(error);
        }
      );
  }
}
