import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FacturaService from './factura.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import VentaService from '@/entities/venta/venta.service';
import { type IVenta } from '@/shared/model/venta.model';
import { Factura, type IFactura } from '@/shared/model/factura.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FacturaUpdate',
  setup() {
    const facturaService = inject('facturaService', () => new FacturaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const factura: Ref<IFactura> = ref(new Factura());

    const ventaService = inject('ventaService', () => new VentaService());

    const ventas: Ref<IVenta[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveFactura = async facturaId => {
      try {
        const res = await facturaService().find(facturaId);
        factura.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.facturaId) {
      retrieveFactura(route.params.facturaId);
    }

    const initRelationships = () => {
      ventaService()
        .retrieve()
        .then(res => {
          ventas.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      numero: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 50 caracteres.', 50),
      },
      fechaEmision: {
        required: validations.required('Este campo es obligatorio.'),
      },
      total: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      activo: {},
      venta: {},
    };
    const v$ = useVuelidate(validationRules, factura as any);
    v$.value.$validate();

    return {
      facturaService,
      alertService,
      factura,
      previousState,
      isSaving,
      currentLanguage,
      ventas,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.factura.id) {
        this.facturaService()
          .update(this.factura)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Factura is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.facturaService()
          .create(this.factura)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Factura is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
