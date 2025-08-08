import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ProveedorService from './proveedor.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IProveedor, Proveedor } from '@/shared/model/proveedor.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProveedorUpdate',
  setup() {
    const proveedorService = inject('proveedorService', () => new ProveedorService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const proveedor: Ref<IProveedor> = ref(new Proveedor());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveProveedor = async proveedorId => {
      try {
        const res = await proveedorService().find(proveedorId);
        proveedor.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.proveedorId) {
      retrieveProveedor(route.params.proveedorId);
    }

    const initRelationships = () => {};

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 50 caracteres.', 50),
      },
      email: {
        maxLength: validations.maxLength('Este campo no puede superar más de 256 caracteres.', 256),
      },
      telefono: {
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      razonSocial: {
        maxLength: validations.maxLength('Este campo no puede superar más de 150 caracteres.', 150),
      },
      rfc: {
        maxLength: validations.maxLength('Este campo no puede superar más de 13 caracteres.', 13),
      },
      activo: {},
      compras: {},
    };
    const v$ = useVuelidate(validationRules, proveedor as any);
    v$.value.$validate();

    return {
      proveedorService,
      alertService,
      proveedor,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.proveedor.id) {
        this.proveedorService()
          .update(this.proveedor)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Proveedor is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.proveedorService()
          .create(this.proveedor)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Proveedor is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
