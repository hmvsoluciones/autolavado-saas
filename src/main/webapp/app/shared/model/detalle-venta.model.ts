import { type IVenta } from '@/shared/model/venta.model';
import { type IProducto } from '@/shared/model/producto.model';

export interface IDetalleVenta {
  id?: number;
  cantidad?: number;
  precioUnitario?: number;
  subtotal?: number;
  venta?: IVenta | null;
  producto?: IProducto | null;
}

export class DetalleVenta implements IDetalleVenta {
  constructor(
    public id?: number,
    public cantidad?: number,
    public precioUnitario?: number,
    public subtotal?: number,
    public venta?: IVenta | null,
    public producto?: IProducto | null,
  ) {}
}
