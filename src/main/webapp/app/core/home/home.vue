<template>
  <section class="home-section container py-5">
    <div class="row align-items-center mb-5">
      <div class="col-lg-6 text-center text-lg-start">
        <h1 class="display-4 fw-bold text-primary">Bienvenido a ERP HMV Soluciones</h1>
        <p class="lead text-muted">Transformamos ideas en soluciones digitales efectivas. Este es tu panel de control inicial.</p>

        <div v-if="authenticated" class="alert alert-success mt-4">
          <font-awesome-icon icon="user" class="me-2" />
          Conectado como: <strong>{{ username }}</strong>
        </div>

        <div v-else class="mt-4">
          <div class="alert alert-warning">
            <font-awesome-icon icon="user" class="me-2" />
            No has iniciado sesión.
            <a @click="showLogin()" class="alert-link" role="button">Haz clic aquí para acceder</a>
          </div>
        </div>
      </div>

      <div class="col-lg-6 text-center">
        <img
          src="https://hmvsoluciones.com/hmv-assets/images/logo-white.png"
          alt="Dashboard illustration"
          class="img-fluid"
          style="max-height: 300px"
        />
      </div>
    </div>

    <div class="row text-center g-4">
      <div class="col-md-4">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body">
            <font-awesome-icon icon="cogs" size="2x" class="text-primary mb-3" />
            <h5 class="card-title">Gestión Inteligente</h5>
            <p class="card-text text-muted">Automatiza tus procesos con nuestras herramientas inteligentes diseñadas para tu negocio.</p>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body">
            <font-awesome-icon icon="cloud" size="2x" class="text-primary mb-3" />
            <h5 class="card-title">Seguridad</h5>
            <p class="card-text text-muted">
              Seguridad robusta para proteger tus datos y los de tus clientes. Cumplimos con los estándares más exigentes.
            </p>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body">
            <font-awesome-icon icon="bell" size="2x" class="text-primary mb-3" />
            <h5 class="card-title">Soporte Ágil</h5>
            <p class="card-text text-muted">Nuestro equipo está listo para ayudarte cuando lo necesites. Atención técnica 24/7.</p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script lang="ts" src="./home.component.ts"></script>

<style scoped>
.home-section {
  background: linear-gradient(to bottom right, #f0f8ff, #ffffff);
  border-radius: 12px;
}
.card {
  transition: transform 0.3s ease;
}
.card:hover {
  transform: translateY(-5px);
}
</style>
