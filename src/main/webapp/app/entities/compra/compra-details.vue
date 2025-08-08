<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="compra">
        <h2 class="jh-entity-heading" data-cy="compraDetailsHeading"><span>Compra</span> {{ compra.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Fecha Compra</span>
          </dt>
          <dd>
            <span>{{ compra.fechaCompra }}</span>
          </dd>
          <dt>
            <span>Total</span>
          </dt>
          <dd>
            <span>{{ compra.total }}</span>
          </dd>
          <dt>
            <span>Proveedor</span>
          </dt>
          <dd>
            <div v-if="compra.proveedor">
              <router-link :to="{ name: 'ProveedorView', params: { proveedorId: compra.proveedor.id } }">{{
                compra.proveedor.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="compra.id" :to="{ name: 'CompraEdit', params: { compraId: compra.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./compra-details.component.ts"></script>
