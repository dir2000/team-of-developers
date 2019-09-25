import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {QualificationModel} from '../model/qualification.model';

@Injectable({
  providedIn: 'root'
})
export class QualificationService {

  path: string;
  qualification: QualificationModel;

  constructor(private http: HttpClient) {
    this.path = '/developers-team/qualifications';
    this.qualification = new QualificationModel();
  }

  setQualification(qualification: QualificationModel) {
    this.qualification = qualification;
  }

  getQualifications() {

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

  getQualification(id: number) {

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

  createQualification(qualification: QualificationModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .post(
        AppComponent.HOST_NAME + this.path,
        qualification,
        options
      );
  }

  updateQualification(qualification: QualificationModel) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return this.http
      .put(
        AppComponent.HOST_NAME + this.path,
        qualification,
        options
      );
  }

  patchQualification(id: number, responsibility: string) {

    const options: any = {
      headers: {
        'Content-Type': 'application/json'

      }
    };

    return this.http
      .patch(
        AppComponent.HOST_NAME + this.path + '/' + id + '/responsibility/' + responsibility,
        options
      );
  }

  deleteQualification(id: number) {

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

