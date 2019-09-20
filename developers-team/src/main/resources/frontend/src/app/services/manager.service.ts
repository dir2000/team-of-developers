import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {ManagerModel} from '../model/manager.model';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {

  path: string;
  manager: ManagerModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/managers';
    this.manager = new ManagerModel();
  }

  setManager(manager: ManagerModel) {
    this.manager = manager;
  }

  getManagers() {

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

  getManager(id: number) {

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

  createManager(manager: ManagerModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        manager,
        options
      );
  }

  updateManager(manager: ManagerModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        manager,
        options
      );
  }

  patchManager(id: number, phone: string) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'

      }
    };

    return this.http
      .patch(
        AppComponent.HOST_NAME + this.path + '/' + id + '/phone/' + phone,
        options
      );
  }

  deleteManager(id: number) {

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

