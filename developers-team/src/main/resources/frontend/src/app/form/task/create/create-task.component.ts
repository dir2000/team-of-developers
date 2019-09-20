import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {TaskModel} from '../../../model/task.model';
import {TaskService} from '../../../services/task.service';
import {StateService} from '../../../services/state.service';

@Component({
  selector: 'app-create-technicaltask',
  templateUrl: './create-task.component.html',
  styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {
  formSubmitted: boolean;
  task: TaskModel;

  constructor(private taskService: TaskService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.task = form.value as TaskModel;
    this.stateService.dropState();
  }

  createTask(form: NgForm) {
    this.dropDataToDefault(form);

    this.taskService.createTask(this.task)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.task = (responseData as TaskModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  updateTask(form: NgForm) {
    this.dropDataToDefault(form);
    this.task.id = this.taskService.task.id;
    this.taskService.updateTask(this.task)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.task = (responseData as TaskModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  patchTask(form: NgForm) {
    this.dropDataToDefault(form);

    this.taskService.patchTask(this.taskService.task.id, this.task.description)
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

