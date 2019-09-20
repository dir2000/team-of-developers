export class ManagerModel {
  public id?: number;
  public name?: string;
  public email?: string;
  public phone?: string;

  constructor(name?: string, id?: number,  email?: string, phone?: string)  {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
  }
}
