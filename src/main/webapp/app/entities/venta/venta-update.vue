<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="autolavadoSaasApp.venta.home.createOrEditLabel" data-cy="VentaCreateUpdateHeading">Crear o editar Venta</h2>
        <div>
          <div class="form-group" v-if="venta.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="venta.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="venta-fechaVenta">Fecha Venta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="venta-fechaVenta"
                  v-model="v$.fechaVenta.$model"
                  name="fechaVenta"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="venta-fechaVenta"
                data-cy="fechaVenta"
                type="text"
                class="form-control"
                name="fechaVenta"
                :class="{ valid: !v$.fechaVenta.$invalid, invalid: v$.fechaVenta.$invalid }"
                v-model="v$.fechaVenta.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaVenta.$anyDirty && v$.fechaVenta.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaVenta.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="venta-total">Total</label>
            <input
              type="number"
              class="form-control"
              name="total"
              id="venta-total"
              data-cy="total"
              :class="{ valid: !v$.total.$invalid, invalid: v$.total.$invalid }"
              v-model.number="v$.total.$model"
              required
            />
            <div v-if="v$.total.$anyDirty && v$.total.$invalid">
              <small class="form-text text-danger" v-for="error of v$.total.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="venta-cliente">Cliente</label>
            <select class="form-control" id="venta-cliente" data-cy="cliente" name="cliente" v-model="venta.cliente">
              <option :value="null"></option>
              <option
                :value="venta.cliente && clienteOption.id === venta.cliente.id ? venta.cliente : clienteOption"
                v-for="clienteOption in clientes"
                :key="clienteOption.id"
              >
                {{ clienteOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./venta-update.component.ts"></script>
