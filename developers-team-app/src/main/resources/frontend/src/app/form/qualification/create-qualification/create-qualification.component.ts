import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {QualificationModel} from '../../../model/qualification.model';
import {QualificationService} from '../../../services/qualification.service';
import {StateService} from '../../../services/state.service';

@Component({
  selector: 'app-create-qualification',
  templateUrl: './create-qualification.component.html',
  styleUrls: ['./create-qualification.component.css']
})
export class CreateQualificationComponent implements OnInit {
  formSubmitted: boolean;
  qualification: QualificationModel;

  constructor(private qualificationService: QualificationService, private stateService: StateService) {
  }

  ngOnInit() {
  }

  dropDataToDefault(form: NgForm) {
    this.formSubmitted = false;
    this.qualification = form.value as QualificationModel;
    this.stateService.dropState();
  }

  createQualification(form: NgForm) {
    this.dropDataToDefault(form);

    this.qualificationService.createQualification(this.qualification)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.qualification = (responseData as QualificationModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  updateQualification(form: NgForm) {
    this.dropDataToDefault(form);
    this.qualification.id = this.qualificationService.qualification.id;
    this.qualificationService.updateQualification(this.qualification)
      .subscribe(responseData => {
          this.formSubmitted = true;
          this.qualification = (responseData as QualificationModel);
        },
        error => {
          console.log(error);
        }
      );
  }

  patchQualification(form: NgForm) {
    this.dropDataToDefault(form);

    this.qualificationService.patchQualification(this.qualificationService.qualification.id, this.qualification.responsibility)
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
