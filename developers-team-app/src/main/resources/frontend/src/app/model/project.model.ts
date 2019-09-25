export class ProjectModel {
  public id?: number;
  public name?: string;
  public description?: string;
  public status?: string;
  public eta?: Date;

  constructor(name?: string, id?: number,  description?: string, status?: string, eta?: Date)  {
    this.id = id;
    this.name = name;
    this.description = description;
    this.status = status;
    this.eta = eta;
  }
}
