<template>
  <div id="app">
    <ribbon></ribbon>

    <!-- Navbar con botón para abrir sidebar -->
    <div id="app-header">
      <jhi-navbar @toggle-sidebar="toggleSidebar"></jhi-navbar>
    </div>

    <!-- Sidebar tipo drawer -->
    <b-sidebar
      id="sidebar-drawer"
      :visible="sidebarVisible"
      @hidden="sidebarVisible = false"
      shadow
      backdrop
      :right="false"
      width="250px"
      class="bg-light"
    >
      <template #title>
        <strong>Menú</strong>
      </template>

      <b-nav vertical class="flex-column">
        <b-nav-item to="/" exact>Inicio</b-nav-item>
        <b-nav-item v-if="hasRole('ROLE_ADMIN')" to="/admin/user-management">Usuarios</b-nav-item>
        <b-nav-item v-if="hasRole('ROLE_USER')" to="/entity/product">Productos</b-nav-item>
        <b-nav-item v-if="hasRole('ROLE_USER')" to="/entity/order">Pedidos</b-nav-item>
      </b-nav>
    </b-sidebar>

    <!-- Contenido principal -->
    <div class="container-fluid mt-3">
      <div class="card jh-card">
        <router-view></router-view>
      </div>

      <!-- Modal Login -->
      <b-modal id="login-page" v-model="loginModalOpen" hide-footer lazy>
        <template #modal-title>
          <span data-cy="loginTitle" id="login-title">Iniciar sesión</span>
        </template>
        <login-form></login-form>
      </b-modal>

      <jhi-footer></jhi-footer>
    </div>
  </div>
</template>

<script lang="ts" src="./app.component.ts"></script>
