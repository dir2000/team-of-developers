export class CustomerModel {
  public id?: number;
  public name?: string;
  public address?: string;
  public phone?: string;

  constructor(id?: number, name?: string, address?: string, phone?: string)  {
    this.id = id;
    this.name = name;
    this.address = address;
    this.phone = phone;
  }
}
