<template>
  <div>
    <h2 id="page-heading" data-cy="ProveedorHeading">
      <span id="proveedor-heading">Proveedors</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'ProveedorCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-proveedor"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Proveedor</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && proveedors && proveedors.length === 0">
      <span>Ningún Proveedors encontrado</span>
    </div>
    <div class="table-responsive" v-if="proveedors && proveedors.length > 0">
      <table class="table table-striped" aria-describedby="proveedors">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('email')">
              <span>Email</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('telefono')">
              <span>Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'telefono'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('razonSocial')">
              <span>Razon Social</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'razonSocial'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('rfc')">
              <span>Rfc</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'rfc'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('activo')">
              <span>Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="proveedor in proveedors" :key="proveedor.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProveedorView', params: { proveedorId: proveedor.id } }">{{ proveedor.id }}</router-link>
            </td>
            <td>{{ proveedor.nombre }}</td>
            <td>{{ proveedor.email }}</td>
            <td>{{ proveedor.telefono }}</td>
            <td>{{ proveedor.razonSocial }}</td>
            <td>{{ proveedor.rfc }}</td>
            <td>{{ proveedor.activo }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProveedorView', params: { proveedorId: proveedor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProveedorEdit', params: { proveedorId: proveedor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(proveedor)"
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
        <span id="autolavadoSaasApp.proveedor.delete.question" data-cy="proveedorDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-proveedor-heading">¿Seguro que quiere eliminar Proveedor {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-proveedor"
            data-cy="entityConfirmDeleteButton"
            @click="removeProveedor()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="proveedors && proveedors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./proveedor.component.ts"></script>
