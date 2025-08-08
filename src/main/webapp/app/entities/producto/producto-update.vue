<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="autolavadoSaasApp.producto.home.createOrEditLabel" data-cy="ProductoCreateUpdateHeading">Crear o editar Producto</h2>
        <div>
          <div class="form-group" v-if="producto.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="producto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="producto-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-descripcion">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="producto-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            />
            <div v-if="v$.descripcion.$anyDirty && v$.descripcion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.descripcion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-precio">Precio</label>
            <input
              type="number"
              class="form-control"
              name="precio"
              id="producto-precio"
              data-cy="precio"
              :class="{ valid: !v$.precio.$invalid, invalid: v$.precio.$invalid }"
              v-model.number="v$.precio.$model"
              required
            />
            <div v-if="v$.precio.$anyDirty && v$.precio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.precio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-cantidad">Cantidad</label>
            <input
              type="number"
              class="form-control"
              name="cantidad"
              id="producto-cantidad"
              data-cy="cantidad"
              :class="{ valid: !v$.cantidad.$invalid, invalid: v$.cantidad.$invalid }"
              v-model.number="v$.cantidad.$model"
              required
            />
            <div v-if="v$.cantidad.$anyDirty && v$.cantidad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.cantidad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-fechaActualizacion">Fecha Actualizacion</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="producto-fechaActualizacion"
                  v-model="v$.fechaActualizacion.$model"
                  name="fechaActualizacion"
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
                id="producto-fechaActualizacion"
                data-cy="fechaActualizacion"
                type="text"
                class="form-control"
                name="fechaActualizacion"
                :class="{ valid: !v$.fechaActualizacion.$invalid, invalid: v$.fechaActualizacion.$invalid }"
                v-model="v$.fechaActualizacion.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaActualizacion.$anyDirty && v$.fechaActualizacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaActualizacion.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-activo">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="producto-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-categoriaProducto">Categoria Producto</label>
            <select
              class="form-control"
              id="producto-categoriaProducto"
              data-cy="categoriaProducto"
              name="categoriaProducto"
              v-model="producto.categoriaProducto"
            >
              <option :value="null"></option>
              <option
                :value="
                  producto.categoriaProducto && categoriaProductoOption.id === producto.categoriaProducto.id
                    ? producto.categoriaProducto
                    : categoriaProductoOption
                "
                v-for="categoriaProductoOption in categoriaProductos"
                :key="categoriaProductoOption.id"
              >
                {{ categoriaProductoOption.id }}
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
<script lang="ts" src="./producto-update.component.ts"></script>
