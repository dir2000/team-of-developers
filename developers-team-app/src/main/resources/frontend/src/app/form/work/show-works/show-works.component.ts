import {Component, OnInit} from '@angular/core';
import {WorkModel} from '../../../model/work.model';
import {WorkService} from '../../../services/work.service';
import {StateService} from '../../../services/state.service';
import {ResponseArray} from '../../../model/responsearray.model';
import {StateEnum} from '../../../enums/state.enum';

@Component({
  selector: 'app-show-work',
  templateUrl: './show-works.component.html',
  styleUrls: ['./show-works.component.css']
})
export class ShowWorksComponent implements OnInit {
  formSubmitted: boolean;
  works: WorkModel[];

  constructor(private workService: WorkService, private stateService: StateService) {
    this.works = [];
  }

  ngOnInit() {

    const response = this.workService.getWorks();

    response.subscribe(responseData => {
        this.works = ((responseData as ResponseArray).response as WorkModel[]);
        console.log(this.works);
      },
      error => {
        console.log(error);
      }
    );

  }

  createWork() {
    this.stateService.setState(StateEnum.CREATE);
    this.workService.work = new WorkModel();
  }

  updateWork(id: number) {
    this.stateService.setState(StateEnum.UPDATE);
    this.workService.setWork(this.works.find(b => b.id === id));
  }

  patchAddress(id: number) {
    this.stateService.setState(StateEnum.PATCH);
    this.workService.setWork(this.works.find(b => b.id === id));
    console.log(this.stateService.state.toString());
  }

  deleteWork(id: number) {
    const response =  this.workService.deleteWork(id);

    response.subscribe( responseData => {
        console.log(responseData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
