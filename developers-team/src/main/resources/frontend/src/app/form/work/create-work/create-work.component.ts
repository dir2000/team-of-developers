import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {WorkModel} from '../../../model/work.model';
import {WorkService} from '../../../services/work.service';
import {StateService} from '../../../services/state.service';

@Component({
  selector: 'app-create-work',
  templateUrl: './create-work.component.html',
  styleUrls: ['./create-work.component.css']
})
export class CreateWorkComponent implements OnInit {
  formSubmitted: boolean;
  work: WorkModel;

  constructor(private workService: WorkService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.work = form.value as WorkModel;
    this.stateService.dropState();
  }

  createWork(form: NgForm) {
    this.dropDataToDefault(form);

    this.workService.createWork(this.work)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.work = (responseData as WorkModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  updateWork(form: NgForm) {
    this.dropDataToDefault(form);
    this.work.id = this.workService.work.id;
    this.workService.updateWork(this.work)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.work = (responseData as WorkModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  patchWork(form: NgForm) {
    this.dropDataToDefault(form);

    this.workService.patchWork(this.workService.work.id, this.work.price.toString())
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
