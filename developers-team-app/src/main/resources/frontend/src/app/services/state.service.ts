import {Injectable} from '@angular/core';
import {StateEnum} from '../enums/state.enum';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StateService {

  state: StateEnum;

  stateObservable: Observable<StateEnum>;

  constructor() {
    this.state = StateEnum.UNDEFINED;
    this.stateObservable = new Observable(subscriber => {
      subscriber.next(this.state);
    });
  }

  getStates() {
    return StateEnum;
  }

  isState(st: StateEnum) {
    return this.state === st;
  }

  setState(st: StateEnum) {
    this.state = st;
  }

  dropState() {
    this.state = StateEnum.UNDEFINED;
  }

}
