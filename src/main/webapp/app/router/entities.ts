import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const CategoriaProducto = () => import('@/entities/categoria-producto/categoria-producto.vue');
const CategoriaProductoUpdate = () => import('@/entities/categoria-producto/categoria-producto-update.vue');
const CategoriaProductoDetails = () => import('@/entities/categoria-producto/categoria-producto-details.vue');

const Producto = () => import('@/entities/producto/producto.vue');
const ProductoUpdate = () => import('@/entities/producto/producto-update.vue');
const ProductoDetails = () => import('@/entities/producto/producto-details.vue');

const Cliente = () => import('@/entities/cliente/cliente.vue');
const ClienteUpdate = () => import('@/entities/cliente/cliente-update.vue');
const ClienteDetails = () => import('@/entities/cliente/cliente-details.vue');

const Proveedor = () => import('@/entities/proveedor/proveedor.vue');
const ProveedorUpdate = () => import('@/entities/proveedor/proveedor-update.vue');
const ProveedorDetails = () => import('@/entities/proveedor/proveedor-details.vue');

const Venta = () => import('@/entities/venta/venta.vue');
const VentaUpdate = () => import('@/entities/venta/venta-update.vue');
const VentaDetails = () => import('@/entities/venta/venta-details.vue');

const DetalleVenta = () => import('@/entities/detalle-venta/detalle-venta.vue');
const DetalleVentaUpdate = () => import('@/entities/detalle-venta/detalle-venta-update.vue');
const DetalleVentaDetails = () => import('@/entities/detalle-venta/detalle-venta-details.vue');

const Compra = () => import('@/entities/compra/compra.vue');
const CompraUpdate = () => import('@/entities/compra/compra-update.vue');
const CompraDetails = () => import('@/entities/compra/compra-details.vue');

const Factura = () => import('@/entities/factura/factura.vue');
const FacturaUpdate = () => import('@/entities/factura/factura-update.vue');
const FacturaDetails = () => import('@/entities/factura/factura-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'categoria-producto',
      name: 'CategoriaProducto',
      component: CategoriaProducto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'categoria-producto/new',
      name: 'CategoriaProductoCreate',
      component: CategoriaProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'categoria-producto/:categoriaProductoId/edit',
      name: 'CategoriaProductoEdit',
      component: CategoriaProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'categoria-producto/:categoriaProductoId/view',
      name: 'CategoriaProductoView',
      component: CategoriaProductoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto',
      name: 'Producto',
      component: Producto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto/new',
      name: 'ProductoCreate',
      component: ProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto/:productoId/edit',
      name: 'ProductoEdit',
      component: ProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto/:productoId/view',
      name: 'ProductoView',
      component: ProductoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente',
      name: 'Cliente',
      component: Cliente,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/new',
      name: 'ClienteCreate',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/edit',
      name: 'ClienteEdit',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/view',
      name: 'ClienteView',
      component: ClienteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proveedor',
      name: 'Proveedor',
      component: Proveedor,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proveedor/new',
      name: 'ProveedorCreate',
      component: ProveedorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proveedor/:proveedorId/edit',
      name: 'ProveedorEdit',
      component: ProveedorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proveedor/:proveedorId/view',
      name: 'ProveedorView',
      component: ProveedorDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'venta',
      name: 'Venta',
      component: Venta,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'venta/new',
      name: 'VentaCreate',
      component: VentaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'venta/:ventaId/edit',
      name: 'VentaEdit',
      component: VentaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'venta/:ventaId/view',
      name: 'VentaView',
      component: VentaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'detalle-venta',
      name: 'DetalleVenta',
      component: DetalleVenta,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'detalle-venta/new',
      name: 'DetalleVentaCreate',
      component: DetalleVentaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'detalle-venta/:detalleVentaId/edit',
      name: 'DetalleVentaEdit',
      component: DetalleVentaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'detalle-venta/:detalleVentaId/view',
      name: 'DetalleVentaView',
      component: DetalleVentaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'compra',
      name: 'Compra',
      component: Compra,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'compra/new',
      name: 'CompraCreate',
      component: CompraUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'compra/:compraId/edit',
      name: 'CompraEdit',
      component: CompraUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'compra/:compraId/view',
      name: 'CompraView',
      component: CompraDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factura',
      name: 'Factura',
      component: Factura,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factura/new',
      name: 'FacturaCreate',
      component: FacturaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factura/:facturaId/edit',
      name: 'FacturaEdit',
      component: FacturaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factura/:facturaId/view',
      name: 'FacturaView',
      component: FacturaDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
