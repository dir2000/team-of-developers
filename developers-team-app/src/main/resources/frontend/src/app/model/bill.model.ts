export class BillModel {
  public id?: number;
  public docDate?: Date;

  constructor(docDate?: Date, id?: number)  {
    this.id = id;
    this.docDate = docDate;
  }
}
