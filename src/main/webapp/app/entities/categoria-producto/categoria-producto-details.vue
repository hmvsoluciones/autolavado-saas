<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="categoriaProducto">
        <h2 class="jh-entity-heading" data-cy="categoriaProductoDetailsHeading">
          <span>Categoria Producto</span> {{ categoriaProducto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ categoriaProducto.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ categoriaProducto.descripcion }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="categoriaProducto.id"
          :to="{ name: 'CategoriaProductoEdit', params: { categoriaProductoId: categoriaProducto.id } }"
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

<script lang="ts" src="./categoria-producto-details.component.ts"></script>
