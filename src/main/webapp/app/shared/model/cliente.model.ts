export interface ICliente {
  id?: number;
  nombre?: string;
  email?: string;
  telefono?: string | null;
  razonSocial?: string | null;
  rfc?: string | null;
  activo?: boolean | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string,
    public email?: string,
    public telefono?: string | null,
    public razonSocial?: string | null,
    public rfc?: string | null,
    public activo?: boolean | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
