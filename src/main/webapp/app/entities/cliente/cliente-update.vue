<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="autolavadoSaasApp.cliente.home.createOrEditLabel" data-cy="ClienteCreateUpdateHeading">Crear o editar Cliente</h2>
        <div>
          <div class="form-group" v-if="cliente.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="cliente.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="cliente-nombre"
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
            <label class="form-control-label" for="cliente-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="cliente-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
              required
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-telefono">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="cliente-telefono"
              data-cy="telefono"
              :class="{ valid: !v$.telefono.$invalid, invalid: v$.telefono.$invalid }"
              v-model="v$.telefono.$model"
            />
            <div v-if="v$.telefono.$anyDirty && v$.telefono.$invalid">
              <small class="form-text text-danger" v-for="error of v$.telefono.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-razonSocial">Razon Social</label>
            <input
              type="text"
              class="form-control"
              name="razonSocial"
              id="cliente-razonSocial"
              data-cy="razonSocial"
              :class="{ valid: !v$.razonSocial.$invalid, invalid: v$.razonSocial.$invalid }"
              v-model="v$.razonSocial.$model"
            />
            <div v-if="v$.razonSocial.$anyDirty && v$.razonSocial.$invalid">
              <small class="form-text text-danger" v-for="error of v$.razonSocial.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-rfc">Rfc</label>
            <input
              type="text"
              class="form-control"
              name="rfc"
              id="cliente-rfc"
              data-cy="rfc"
              :class="{ valid: !v$.rfc.$invalid, invalid: v$.rfc.$invalid }"
              v-model="v$.rfc.$model"
            />
            <div v-if="v$.rfc.$anyDirty && v$.rfc.$invalid">
              <small class="form-text text-danger" v-for="error of v$.rfc.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-activo">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="cliente-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
            />
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
<script lang="ts" src="./cliente-update.component.ts"></script>
