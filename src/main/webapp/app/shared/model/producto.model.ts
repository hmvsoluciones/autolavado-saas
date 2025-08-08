import { type ICategoriaProducto } from '@/shared/model/categoria-producto.model';

export interface IProducto {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  precio?: number;
  cantidad?: number;
  fechaActualizacion?: Date;
  activo?: boolean | null;
  categoriaProducto?: ICategoriaProducto | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
    public precio?: number,
    public cantidad?: number,
    public fechaActualizacion?: Date,
    public activo?: boolean | null,
    public categoriaProducto?: ICategoriaProducto | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
