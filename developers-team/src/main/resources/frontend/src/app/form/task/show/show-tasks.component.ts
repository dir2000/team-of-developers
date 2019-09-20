import {Component, OnInit} from '@angular/core';
import {TaskModel} from '../../../model/task.model';
import {ResponseArray} from '../../../model/responsearray.model';
import {StateService} from '../../../services/state.service';
import {TaskService} from '../../../services/task.service';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-task',
  templateUrl: './show-tasks.component.html',
  styleUrls: ['./show-tasks.component.css']
})
export class ShowTasksComponent implements OnInit {

  tasks: TaskModel[];

  constructor(private taskService: TaskService, private stateService: StateService) {
    this.tasks = [];
  }

  ngOnInit() {

    const response = this.taskService.getTasks();

    response.subscribe(responseData => {
        this.tasks = ((responseData as ResponseArray).response as TaskModel[]);
        console.log(this.tasks);
      },
      error => {
        console.log(error);
      }
    );

  }

  createTask() {
    this.stateService.setState(StateEnum.CREATE);
    this.taskService.task = new TaskModel();
  }

  updateTask(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.taskService.setTask(this.tasks.find(b => b.id === id));
  }

  patchDesciption(id: number) {
    this.stateService.setState(StateEnum.PATCH);
    this.taskService.setTask(this.tasks.find(b => b.id === id));
    console.log(this.stateService.state.toString());
  }

  deleteTask(id: number) {
    const response =  this.taskService.deleteTask(id);

    response.subscribe( responseData => {
        console.log(responseData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
