<template>
  <div>
    <h2 id="page-heading" data-cy="CategoriaProductoHeading">
      <span id="categoria-producto-heading">Categoria Productos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'CategoriaProductoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-categoria-producto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Categoria Producto</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && categoriaProductos && categoriaProductos.length === 0">
      <span>Ningún Categoria Productos encontrado</span>
    </div>
    <div class="table-responsive" v-if="categoriaProductos && categoriaProductos.length > 0">
      <table class="table table-striped" aria-describedby="categoriaProductos">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('descripcion')">
              <span>Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="categoriaProducto in categoriaProductos" :key="categoriaProducto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CategoriaProductoView', params: { categoriaProductoId: categoriaProducto.id } }">{{
                categoriaProducto.id
              }}</router-link>
            </td>
            <td>{{ categoriaProducto.nombre }}</td>
            <td>{{ categoriaProducto.descripcion }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CategoriaProductoView', params: { categoriaProductoId: categoriaProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'CategoriaProductoEdit', params: { categoriaProductoId: categoriaProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(categoriaProducto)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Eliminar</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="autolavadoSaasApp.categoriaProducto.delete.question" data-cy="categoriaProductoDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-categoriaProducto-heading">¿Seguro que quiere eliminar Categoria Producto {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-categoriaProducto"
            data-cy="entityConfirmDeleteButton"
            @click="removeCategoriaProducto()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="categoriaProductos && categoriaProductos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./categoria-producto.component.ts"></script>
