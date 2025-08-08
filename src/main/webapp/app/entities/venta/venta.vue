<template>
  <div>
    <h2 id="page-heading" data-cy="VentaHeading">
      <span id="venta-heading">Ventas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'VentaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-venta"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Venta</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && ventas && ventas.length === 0">
      <span>Ningún Ventas encontrado</span>
    </div>
    <div class="table-responsive" v-if="ventas && ventas.length > 0">
      <table class="table table-striped" aria-describedby="ventas">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaVenta')">
              <span>Fecha Venta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaVenta'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('total')">
              <span>Total</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'total'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cliente.id')">
              <span>Cliente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cliente.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="venta in ventas" :key="venta.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VentaView', params: { ventaId: venta.id } }">{{ venta.id }}</router-link>
            </td>
            <td>{{ venta.fechaVenta }}</td>
            <td>{{ venta.total }}</td>
            <td>
              <div v-if="venta.cliente">
                <router-link :to="{ name: 'ClienteView', params: { clienteId: venta.cliente.id } }">{{ venta.cliente.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VentaView', params: { ventaId: venta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VentaEdit', params: { ventaId: venta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(venta)"
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
        <span id="autolavadoSaasApp.venta.delete.question" data-cy="ventaDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-venta-heading">¿Seguro que quiere eliminar Venta {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-venta"
            data-cy="entityConfirmDeleteButton"
            @click="removeVenta()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="ventas && ventas.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./venta.component.ts"></script>
