import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import DetalleVentaService from './detalle-venta.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import VentaService from '@/entities/venta/venta.service';
import { type IVenta } from '@/shared/model/venta.model';
import ProductoService from '@/entities/producto/producto.service';
import { type IProducto } from '@/shared/model/producto.model';
import { DetalleVenta, type IDetalleVenta } from '@/shared/model/detalle-venta.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DetalleVentaUpdate',
  setup() {
    const detalleVentaService = inject('detalleVentaService', () => new DetalleVentaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const detalleVenta: Ref<IDetalleVenta> = ref(new DetalleVenta());

    const ventaService = inject('ventaService', () => new VentaService());

    const ventas: Ref<IVenta[]> = ref([]);

    const productoService = inject('productoService', () => new ProductoService());

    const productos: Ref<IProducto[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDetalleVenta = async detalleVentaId => {
      try {
        const res = await detalleVentaService().find(detalleVentaId);
        detalleVenta.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.detalleVentaId) {
      retrieveDetalleVenta(route.params.detalleVentaId);
    }

    const initRelationships = () => {
      ventaService()
        .retrieve()
        .then(res => {
          ventas.value = res.data;
        });
      productoService()
        .retrieve()
        .then(res => {
          productos.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      cantidad: {
        required: validations.required('Este campo es obligatorio.'),
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 1.', 1),
      },
      precioUnitario: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      subtotal: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      venta: {},
      producto: {},
    };
    const v$ = useVuelidate(validationRules, detalleVenta as any);
    v$.value.$validate();

    return {
      detalleVentaService,
      alertService,
      detalleVenta,
      previousState,
      isSaving,
      currentLanguage,
      ventas,
      productos,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.detalleVenta.id) {
        this.detalleVentaService()
          .update(this.detalleVenta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A DetalleVenta is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.detalleVentaService()
          .create(this.detalleVenta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A DetalleVenta is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
