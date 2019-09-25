export class QualificationModel {
  public id?: number;
  public name?: string;
  public responsibility?: string;

  constructor(id?: number, name?: string, responsibility?: string)  {
    this.id = id;
    this.name = name;
    this.responsibility = responsibility;
  }
}
