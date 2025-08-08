import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import VentaService from './venta.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ClienteService from '@/entities/cliente/cliente.service';
import { type ICliente } from '@/shared/model/cliente.model';
import { type IVenta, Venta } from '@/shared/model/venta.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'VentaUpdate',
  setup() {
    const ventaService = inject('ventaService', () => new VentaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const venta: Ref<IVenta> = ref(new Venta());

    const clienteService = inject('clienteService', () => new ClienteService());

    const clientes: Ref<ICliente[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveVenta = async ventaId => {
      try {
        const res = await ventaService().find(ventaId);
        venta.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.ventaId) {
      retrieveVenta(route.params.ventaId);
    }

    const initRelationships = () => {
      clienteService()
        .retrieve()
        .then(res => {
          clientes.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      fechaVenta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      total: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      cliente: {},
      detalleVentas: {},
      facturas: {},
    };
    const v$ = useVuelidate(validationRules, venta as any);
    v$.value.$validate();

    return {
      ventaService,
      alertService,
      venta,
      previousState,
      isSaving,
      currentLanguage,
      clientes,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.venta.id) {
        this.ventaService()
          .update(this.venta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Venta is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.ventaService()
          .create(this.venta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Venta is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
