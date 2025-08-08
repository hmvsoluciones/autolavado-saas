<template>
  <div>
    <h2 id="page-heading" data-cy="FacturaHeading">
      <span id="factura-heading">Facturas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'FacturaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-factura"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Factura</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && facturas && facturas.length === 0">
      <span>Ningún Facturas encontrado</span>
    </div>
    <div class="table-responsive" v-if="facturas && facturas.length > 0">
      <table class="table table-striped" aria-describedby="facturas">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('numero')">
              <span>Numero</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numero'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaEmision')">
              <span>Fecha Emision</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaEmision'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('total')">
              <span>Total</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'total'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('activo')">
              <span>Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('venta.id')">
              <span>Venta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'venta.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="factura in facturas" :key="factura.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FacturaView', params: { facturaId: factura.id } }">{{ factura.id }}</router-link>
            </td>
            <td>{{ factura.numero }}</td>
            <td>{{ factura.fechaEmision }}</td>
            <td>{{ factura.total }}</td>
            <td>{{ factura.activo }}</td>
            <td>
              <div v-if="factura.venta">
                <router-link :to="{ name: 'VentaView', params: { ventaId: factura.venta.id } }">{{ factura.venta.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FacturaView', params: { facturaId: factura.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FacturaEdit', params: { facturaId: factura.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(factura)"
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
        <span id="autolavadoSaasApp.factura.delete.question" data-cy="facturaDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-factura-heading">¿Seguro que quiere eliminar Factura {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-factura"
            data-cy="entityConfirmDeleteButton"
            @click="removeFactura()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="facturas && facturas.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./factura.component.ts"></script>
