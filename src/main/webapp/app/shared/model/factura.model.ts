import { type IVenta } from '@/shared/model/venta.model';

export interface IFactura {
  id?: number;
  numero?: string;
  fechaEmision?: Date;
  total?: number;
  activo?: boolean | null;
  venta?: IVenta | null;
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public numero?: string,
    public fechaEmision?: Date,
    public total?: number,
    public activo?: boolean | null,
    public venta?: IVenta | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
