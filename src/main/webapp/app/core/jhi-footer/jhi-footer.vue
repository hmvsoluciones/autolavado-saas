<template>
  <footer class="jh-footer">
    <div class="container-fluid jh-footer-container">
      <!-- Fila 1: Contenido principal -->
      <div class="row jh-footer-row justify-content-between">
        <!-- Columna 1 -->
        <div class="col-lg-4 jh-footer-col mb-4 mb-lg-0">
          <h5 class="jh-footer-title">Autosiga</h5>
          <p class="jh-footer-text">Solución profesional para gestión de autolavados</p>
        </div>

        <!-- Columna 2 -->
        <div class="col-lg-4 jh-footer-col mb-4 mb-lg-0">
          <h5 class="jh-footer-title">Enlaces</h5>
          <div class="jh-footer-links">
            <a href="#" class="jh-footer-link d-flex align-items-center">
              <font-awesome-icon icon="house" class="mr-2" />
              Inicio
            </a>
            <a href="#" class="jh-footer-link d-flex align-items-center"> <font-awesome-icon :icon="['fas', 'house']" />Servicios </a>
          </div>
        </div>

        <!-- Columna 3 -->
        <div class="col-lg-4 jh-footer-col">
          <h5 class="jh-footer-title">Contacto</h5>
          <div class="jh-footer-contact">
            <div class="jh-footer-contact-item d-flex align-items-center mb-2">+52 55 1234 5678</div>
            <div class="jh-footer-contact-item d-flex align-items-center">info@autolavado.com</div>
          </div>
        </div>
      </div>

      <!-- Fila 2: Copyright -->
      <div class="row jh-footer-copyright-row">
        <div class="col text-center py-3">
          <p class="jh-footer-copyright">
            &copy; {{ currentYear }} Autosiga<a target="_blank" :href="jhiFooter.website">HMV Soluciones</a> Todos los derechos reservados.
            version: {{ jhiFooter.version }}
          </p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script lang="ts" src="./jhi-footer.component.ts"></script>

<style scoped>
.jh-footer {
  background: linear-gradient(135deg, #fafafa, #e0f1e6);
  color: rgb(89, 89, 89);
  width: 100%;
  padding: 0 !important;
  margin: 0 !important;
}

.jh-footer-container {
  padding-left: 0;
  padding-right: 0;
  max-width: 100%;
}

/* Corrección específica para rows/cols */
.jh-footer-row {
  margin-left: 0 !important;
  margin-right: 0 !important;
  padding: 2rem 1.5rem;
  display: flex;
  flex-wrap: wrap;
}

.jh-footer-col {
  padding: 0 1.5rem !important;
  flex: 1 0 300px;
  max-width: 100% !important;
}

/* Estilos visuales */
.jh-footer-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: #1c323d;
}

.jh-footer-text {
  opacity: 0.8;
  line-height: 1.5;
}

.jh-footer-links {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.jh-footer-link {
  color: rgb(3, 3, 3) !important;
  text-decoration: none !important;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
}

.jh-footer-link:hover {
  color: #4cc9f0 !important;
  padding-left: 5px;
}

.jh-footer-contact-item {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}

.jh-footer-copyright-row {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.jh-footer-copyright {
  font-size: 0.8rem;
  opacity: 0.7;
  margin: 0;
}

/* Media queries para responsive */
@media (max-width: 992px) {
  .jh-footer-col {
    flex: 1 0 50%;
    margin-bottom: 1.5rem;
  }
}

@media (max-width: 576px) {
  .jh-footer-col {
    flex: 1 0 100%;
  }

  .jh-footer-row {
    padding: 1.5rem 1rem;
  }
}
</style>
