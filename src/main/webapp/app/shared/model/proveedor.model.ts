export interface IProveedor {
  id?: number;
  nombre?: string;
  email?: string | null;
  telefono?: string | null;
  razonSocial?: string | null;
  rfc?: string | null;
  activo?: boolean | null;
}

export class Proveedor implements IProveedor {
  constructor(
    public id?: number,
    public nombre?: string,
    public email?: string | null,
    public telefono?: string | null,
    public razonSocial?: string | null,
    public rfc?: string | null,
    public activo?: boolean | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
