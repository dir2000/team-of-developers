import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {TaskModel} from '../model/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  path: string;
  task: TaskModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/tasks';
    this.task = new TaskModel();
  }

  setTask(task: TaskModel) {
    this.task = task;
  }

  getTasks() {

    const options: any = {
      headers: {
        Accept: 'application/json'
      }
    };

    return this.http
      .get(
        AppComponent.HOST_NAME + this.path,
        options
      );
  }

  getTask(id: number) {

    const options: any = {
      headers: {
        Accept: 'application/json'
      }
    };

    return this.http
      .get(
        AppComponent.HOST_NAME + this.path + '/' + id,
        options
      );
  }

  createTask(task: TaskModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        task,
        options
      );
  }

  updateTask(task: TaskModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        task,
        options
      );
  }

  patchTask(id: number, description: string) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'

      }
    };

    return this.http
      .patch(
        AppComponent.HOST_NAME + this.path + '/' + id + '/description/' + description,
        options
      );
  }

  deleteTask(id: number) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .delete(
        AppComponent.HOST_NAME + this.path + '/' + id,
        options
      );
  }

}

