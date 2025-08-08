<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="venta">
        <h2 class="jh-entity-heading" data-cy="ventaDetailsHeading"><span>Venta</span> {{ venta.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Fecha Venta</span>
          </dt>
          <dd>
            <span>{{ venta.fechaVenta }}</span>
          </dd>
          <dt>
            <span>Total</span>
          </dt>
          <dd>
            <span>{{ venta.total }}</span>
          </dd>
          <dt>
            <span>Cliente</span>
          </dt>
          <dd>
            <div v-if="venta.cliente">
              <router-link :to="{ name: 'ClienteView', params: { clienteId: venta.cliente.id } }">{{ venta.cliente.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="venta.id" :to="{ name: 'VentaEdit', params: { ventaId: venta.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./venta-details.component.ts"></script>
