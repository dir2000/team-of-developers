import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {ProjectModel} from '../model/project.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  path: string;
  project: ProjectModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/projects';
    this.project = new ProjectModel();
  }

  setProject(project: ProjectModel) {
    this.project = project;
  }

  getProjects() {

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

  getProject(id: number) {

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

  createProject(project: ProjectModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        project,
        options
      );
  }

  updateProject(project: ProjectModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        project,
        options
      );
  }

  patchProject(id: number, description: string) {

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

  deleteProject(id: number) {

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

