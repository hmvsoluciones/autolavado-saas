<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="producto">
        <h2 class="jh-entity-heading" data-cy="productoDetailsHeading"><span>Producto</span> {{ producto.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ producto.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ producto.descripcion }}</span>
          </dd>
          <dt>
            <span>Precio</span>
          </dt>
          <dd>
            <span>{{ producto.precio }}</span>
          </dd>
          <dt>
            <span>Cantidad</span>
          </dt>
          <dd>
            <span>{{ producto.cantidad }}</span>
          </dd>
          <dt>
            <span>Fecha Actualizacion</span>
          </dt>
          <dd>
            <span>{{ producto.fechaActualizacion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ producto.activo }}</span>
          </dd>
          <dt>
            <span>Categoria Producto</span>
          </dt>
          <dd>
            <div v-if="producto.categoriaProducto">
              <router-link :to="{ name: 'CategoriaProductoView', params: { categoriaProductoId: producto.categoriaProducto.id } }">{{
                producto.categoriaProducto.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="producto.id" :to="{ name: 'ProductoEdit', params: { productoId: producto.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./producto-details.component.ts"></script>
