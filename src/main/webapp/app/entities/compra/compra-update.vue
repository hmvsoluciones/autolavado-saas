<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="autolavadoSaasApp.compra.home.createOrEditLabel" data-cy="CompraCreateUpdateHeading">Crear o editar Compra</h2>
        <div>
          <div class="form-group" v-if="compra.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="compra.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="compra-fechaCompra">Fecha Compra</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="compra-fechaCompra"
                  v-model="v$.fechaCompra.$model"
                  name="fechaCompra"
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
                id="compra-fechaCompra"
                data-cy="fechaCompra"
                type="text"
                class="form-control"
                name="fechaCompra"
                :class="{ valid: !v$.fechaCompra.$invalid, invalid: v$.fechaCompra.$invalid }"
                v-model="v$.fechaCompra.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaCompra.$anyDirty && v$.fechaCompra.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaCompra.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="compra-total">Total</label>
            <input
              type="number"
              class="form-control"
              name="total"
              id="compra-total"
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
            <label class="form-control-label" for="compra-proveedor">Proveedor</label>
            <select class="form-control" id="compra-proveedor" data-cy="proveedor" name="proveedor" v-model="compra.proveedor">
              <option :value="null"></option>
              <option
                :value="compra.proveedor && proveedorOption.id === compra.proveedor.id ? compra.proveedor : proveedorOption"
                v-for="proveedorOption in proveedors"
                :key="proveedorOption.id"
              >
                {{ proveedorOption.id }}
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
<script lang="ts" src="./compra-update.component.ts"></script>
