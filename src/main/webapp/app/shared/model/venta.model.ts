import { type ICliente } from '@/shared/model/cliente.model';

export interface IVenta {
  id?: number;
  fechaVenta?: Date;
  total?: number;
  cliente?: ICliente | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public fechaVenta?: Date,
    public total?: number,
    public cliente?: ICliente | null,
  ) {}
}
