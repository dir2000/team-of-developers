import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {DeveloperModel} from '../model/developer.model';

@Injectable({
  providedIn: 'root'
})
export class DeveloperService {

  path: string;
  developer: DeveloperModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/developers';
  }

  setDeveloper(developer: DeveloperModel) {
    this.developer = developer;
  }

  getDevelopers() {

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

  getDeveloper(id: number) {

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

  createDeveloper(developer: DeveloperModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        developer,
        options
      );
  }

  updateDeveloper(developer: DeveloperModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        developer,
        options
      );
  }

  deleteDeveloper(id: number) {

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

