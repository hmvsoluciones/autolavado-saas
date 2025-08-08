<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="autolavadoSaasApp.factura.home.createOrEditLabel" data-cy="FacturaCreateUpdateHeading">Crear o editar Factura</h2>
        <div>
          <div class="form-group" v-if="factura.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="factura.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="factura-numero">Numero</label>
            <input
              type="text"
              class="form-control"
              name="numero"
              id="factura-numero"
              data-cy="numero"
              :class="{ valid: !v$.numero.$invalid, invalid: v$.numero.$invalid }"
              v-model="v$.numero.$model"
              required
            />
            <div v-if="v$.numero.$anyDirty && v$.numero.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numero.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="factura-fechaEmision">Fecha Emision</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="factura-fechaEmision"
                  v-model="v$.fechaEmision.$model"
                  name="fechaEmision"
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
                id="factura-fechaEmision"
                data-cy="fechaEmision"
                type="text"
                class="form-control"
                name="fechaEmision"
                :class="{ valid: !v$.fechaEmision.$invalid, invalid: v$.fechaEmision.$invalid }"
                v-model="v$.fechaEmision.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaEmision.$anyDirty && v$.fechaEmision.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaEmision.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="factura-total">Total</label>
            <input
              type="number"
              class="form-control"
              name="total"
              id="factura-total"
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
            <label class="form-control-label" for="factura-activo">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="factura-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="factura-venta">Venta</label>
            <select class="form-control" id="factura-venta" data-cy="venta" name="venta" v-model="factura.venta">
              <option :value="null"></option>
              <option
                :value="factura.venta && ventaOption.id === factura.venta.id ? factura.venta : ventaOption"
                v-for="ventaOption in ventas"
                :key="ventaOption.id"
              >
                {{ ventaOption.id }}
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
<script lang="ts" src="./factura-update.component.ts"></script>
