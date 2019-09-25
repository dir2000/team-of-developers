import {Component, OnInit} from '@angular/core';
import {QualificationModel} from '../../../model/qualification.model';
import {QualificationService} from '../../../services/qualification.service';
import {StateService} from '../../../services/state.service';
import {ResponseArray} from '../../../model/responsearray.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-qualification',
  templateUrl: './show-qualifications.component.html',
  styleUrls: ['./show-qualifications.component.css']
})
export class ShowQualificationsComponent implements OnInit {
  formSubmitted: boolean;
  qualifications: QualificationModel[];

  constructor(private qualificationService: QualificationService, private stateService: StateService) {
    this.qualifications = [];
  }

  ngOnInit() {

    const response = this.qualificationService.getQualifications();

    response.subscribe(responseData => {
        this.qualifications = ((responseData as ResponseArray).response as QualificationModel[]);
        console.log(this.qualifications);
      },
      error => {
        console.log(error);
      }
    );

  }

  createQualification() {
    this.stateService.setState(StateEnum.CREATE);
    this.qualificationService.qualification = new QualificationModel();
  }

  updateQualification(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.qualificationService.setQualification(this.qualifications.find(b => b.id === id));
  }

  patchAddress(id: number) {
    this.stateService.setState(StateEnum.PATCH);
    this.qualificationService.setQualification(this.qualifications.find(b => b.id === id));
    console.log(this.stateService.state.toString());
  }

  deleteQualification(id: number) {
    const response =  this.qualificationService.deleteQualification(id);

    response.subscribe( responseData => {
        console.log(responseData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
