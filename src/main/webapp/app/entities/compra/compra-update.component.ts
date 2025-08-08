import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CompraService from './compra.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ProveedorService from '@/entities/proveedor/proveedor.service';
import { type IProveedor } from '@/shared/model/proveedor.model';
import { Compra, type ICompra } from '@/shared/model/compra.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CompraUpdate',
  setup() {
    const compraService = inject('compraService', () => new CompraService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const compra: Ref<ICompra> = ref(new Compra());

    const proveedorService = inject('proveedorService', () => new ProveedorService());

    const proveedors: Ref<IProveedor[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCompra = async compraId => {
      try {
        const res = await compraService().find(compraId);
        compra.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.compraId) {
      retrieveCompra(route.params.compraId);
    }

    const initRelationships = () => {
      proveedorService()
        .retrieve()
        .then(res => {
          proveedors.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      fechaCompra: {
        required: validations.required('Este campo es obligatorio.'),
      },
      total: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      proveedor: {},
    };
    const v$ = useVuelidate(validationRules, compra as any);
    v$.value.$validate();

    return {
      compraService,
      alertService,
      compra,
      previousState,
      isSaving,
      currentLanguage,
      proveedors,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.compra.id) {
        this.compraService()
          .update(this.compra)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Compra is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.compraService()
          .create(this.compra)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Compra is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
