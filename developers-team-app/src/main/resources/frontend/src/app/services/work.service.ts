import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {WorkModel} from '../model/work.model';

@Injectable({
  providedIn: 'root'
})
export class WorkService {

  path: string;
  work: WorkModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/works';
    this.work = new WorkModel();
  }

  setWork(work: WorkModel) {
    this.work = work;
  }

  getWorks() {

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

  getWork(id: number) {

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

  createWork(work: WorkModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        work,
        options
      );
  }

  updateWork(work: WorkModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        work,
        options
      );
  }

  patchWork(id: number, price: string) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'

      }
    };

    return this.http
      .patch(
        AppComponent.HOST_NAME + this.path + '/' + id + '/price/' + price,
        options
      );
  }

  deleteWork(id: number) {

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

