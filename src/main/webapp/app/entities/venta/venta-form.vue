<template>
  <div class="container">
    <h2>Nueva Venta</h2>

    <div class="form-group row">
      <label>Producto:</label>
      <select v-model="productoSeleccionado" class="form-control">
        <option :value="null">-- Seleccionar --</option>
        <option v-for="p in productos" :key="p.id" :value="p">{{ p.nombre }} - ${{ p.precio }}</option>
      </select>

      <label>Cantidad:</label>
      <input type="number" v-model.number="cantidad" min="1" class="form-control" />

      <button @click="agregarProducto" class="btn btn-primary mt-2">Agregar</button>
    </div>

    <table class="table mt-3">
      <thead>
        <tr>
          <th>Producto</th>
          <th>Cantidad</th>
          <th>Precio Unitario</th>
          <th>Subtotal</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in carrito" :key="index">
          <td>{{ item.producto.nombre }}</td>
          <td>{{ item.cantidad }}</td>
          <td>{{ item.precioUnitario }}</td>
          <td>{{ (item.cantidad * item.precioUnitario).toFixed(2) }}</td>
          <td><button class="btn btn-danger btn-sm" @click="eliminarProducto(index)">X</button></td>
        </tr>
      </tbody>
    </table>

    <h4>Total: ${{ total.toFixed(2) }}</h4>

    <button @click="guardarVenta" class="btn btn-success mt-2">Guardar y Generar Ticket</button>

    <!-- Modal ticket -->
    <div v-if="mostrarTicket" id="ticket">
      <h3>Mi Tienda</h3>
      <p>Fecha: {{ ventaGuardada.fechaVenta }}</p>
      <hr />
      <table>
        <tr v-for="d in ventaGuardada.detalles" :key="d.producto.id">
          <td>{{ d.producto.nombre }}</td>
          <td>{{ d.cantidad }} x ${{ d.precioUnitario }}</td>
          <td>${{ d.subtotal }}</td>
        </tr>
      </table>
      <hr />
      <h4>Total: ${{ ventaGuardada.total }}</h4>
      <button @click="imprimirTicket" class="btn btn-primary">Imprimir</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'VentaForm',
  data() {
    return {
      productos: [],
      productoSeleccionado: null,
      cantidad: 1,
      carrito: [],
      total: 0,
      mostrarTicket: false,
      ventaGuardada: {},
    };
  },
  created() {
    this.cargarProductos();
  },
  methods: {
    cargarProductos() {
      axios.get('/api/productos').then(res => {
        this.productos = res.data;
      });
    },
    agregarProducto() {
      if (!this.productoSeleccionado || this.cantidad < 1) return;
      this.carrito.push({
        producto: this.productoSeleccionado,
        cantidad: this.cantidad,
        precioUnitario: this.productoSeleccionado.precio,
      });
      this.calcularTotal();
      this.productoSeleccionado = null;
      this.cantidad = 1;
    },
    eliminarProducto(index) {
      this.carrito.splice(index, 1);
      this.calcularTotal();
    },
    calcularTotal() {
      this.total = this.carrito.reduce((sum, item) => sum + item.cantidad * item.precioUnitario, 0);
    },
    guardarVenta() {
      const ventaRequest = {
        venta: {},
        detalles: this.carrito.map(c => ({
          producto: { id: c.producto.id },
          cantidad: c.cantidad,
          precioUnitario: c.precioUnitario,
          subtotal: c.cantidad * c.precioUnitario,
        })),
      };

      axios.post('/api/ventas/crear', ventaRequest).then(res => {
        this.ventaGuardada = {
          ...res.data,
          detalles: ventaRequest.detalles,
        };
        this.mostrarTicket = true;
      });
    },
    imprimirTicket() {
      const printContents = document.getElementById('ticket').innerHTML;
      const w = window.open('', '', 'width=300,height=400');
      w.document.write(printContents);
      w.print();
      w.close();
    },
  },
};
</script>

<style scoped>
#ticket {
  font-family: monospace;
  width: 250px;
}
table {
  font-size: 12px;
  width: 100%;
}
</style>
