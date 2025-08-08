import { type IProveedor } from '@/shared/model/proveedor.model';

export interface ICompra {
  id?: number;
  fechaCompra?: Date;
  total?: number;
  proveedor?: IProveedor | null;
}

export class Compra implements ICompra {
  constructor(
    public id?: number,
    public fechaCompra?: Date,
    public total?: number,
    public proveedor?: IProveedor | null,
  ) {}
}
