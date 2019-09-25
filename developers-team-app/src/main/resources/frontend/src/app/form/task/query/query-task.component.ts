import { Component, OnInit } from '@angular/core';
import {TaskModel} from '../../../model/task.model';
import {TaskService} from '../../../services/task.service';
import {StateService} from '../../../services/state.service';
import {ResponseSingle} from '../../../model/responsesingle.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-query',
  templateUrl: './query-task.component.html',
  styleUrls: ['./query-task.component.css']
})
export class QueryTaskComponent implements OnInit {

  formSubmitted = false;
  task: TaskModel;

  constructor(private taskService: TaskService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  queryTask(id: number) {

    this.formSubmitted = false;

    this.taskService.getTask(id)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.task = (responseData as ResponseSingle).dto as TaskModel;
          this.stateService.setState(StateEnum.UPDATE);
          this.taskService.setTask(this.task);
        },
        error => {
          console.log(error);
        }
      );
  }

}
