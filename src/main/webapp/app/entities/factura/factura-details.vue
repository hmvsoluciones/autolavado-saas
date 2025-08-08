<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="factura">
        <h2 class="jh-entity-heading" data-cy="facturaDetailsHeading"><span>Factura</span> {{ factura.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Numero</span>
          </dt>
          <dd>
            <span>{{ factura.numero }}</span>
          </dd>
          <dt>
            <span>Fecha Emision</span>
          </dt>
          <dd>
            <span>{{ factura.fechaEmision }}</span>
          </dd>
          <dt>
            <span>Total</span>
          </dt>
          <dd>
            <span>{{ factura.total }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ factura.activo }}</span>
          </dd>
          <dt>
            <span>Venta</span>
          </dt>
          <dd>
            <div v-if="factura.venta">
              <router-link :to="{ name: 'VentaView', params: { ventaId: factura.venta.id } }">{{ factura.venta.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="factura.id" :to="{ name: 'FacturaEdit', params: { facturaId: factura.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./factura-details.component.ts"></script>
