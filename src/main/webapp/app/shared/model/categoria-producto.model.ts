export interface ICategoriaProducto {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
}

export class CategoriaProducto implements ICategoriaProducto {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
  ) {}
}
