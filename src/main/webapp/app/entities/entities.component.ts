import { defineComponent, provide } from 'vue';

import CategoriaProductoService from './categoria-producto/categoria-producto.service';
import ProductoService from './producto/producto.service';
import ClienteService from './cliente/cliente.service';
import ProveedorService from './proveedor/proveedor.service';
import VentaService from './venta/venta.service';
import DetalleVentaService from './detalle-venta/detalle-venta.service';
import CompraService from './compra/compra.service';
import FacturaService from './factura/factura.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('categoriaProductoService', () => new CategoriaProductoService());
    provide('productoService', () => new ProductoService());
    provide('clienteService', () => new ClienteService());
    provide('proveedorService', () => new ProveedorService());
    provide('ventaService', () => new VentaService());
    provide('detalleVentaService', () => new DetalleVentaService());
    provide('compraService', () => new CompraService());
    provide('facturaService', () => new FacturaService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
