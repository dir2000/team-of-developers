export class TaskModel {
  public id?: number;
  public title?: string;
  public description?: string;

  constructor(id?: number, title?: string, description?: string)  {
    this.id = id;
    this.title = title;
    this.description = description;
  }
}
