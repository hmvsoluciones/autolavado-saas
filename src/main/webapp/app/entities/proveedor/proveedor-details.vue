<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="proveedor">
        <h2 class="jh-entity-heading" data-cy="proveedorDetailsHeading"><span>Proveedor</span> {{ proveedor.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ proveedor.nombre }}</span>
          </dd>
          <dt>
            <span>Email</span>
          </dt>
          <dd>
            <span>{{ proveedor.email }}</span>
          </dd>
          <dt>
            <span>Telefono</span>
          </dt>
          <dd>
            <span>{{ proveedor.telefono }}</span>
          </dd>
          <dt>
            <span>Razon Social</span>
          </dt>
          <dd>
            <span>{{ proveedor.razonSocial }}</span>
          </dd>
          <dt>
            <span>Rfc</span>
          </dt>
          <dd>
            <span>{{ proveedor.rfc }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ proveedor.activo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="proveedor.id"
          :to="{ name: 'ProveedorEdit', params: { proveedorId: proveedor.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./proveedor-details.component.ts"></script>
