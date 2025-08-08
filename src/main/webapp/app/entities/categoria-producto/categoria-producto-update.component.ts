import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CategoriaProductoService from './categoria-producto.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { CategoriaProducto, type ICategoriaProducto } from '@/shared/model/categoria-producto.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CategoriaProductoUpdate',
  setup() {
    const categoriaProductoService = inject('categoriaProductoService', () => new CategoriaProductoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const categoriaProducto: Ref<ICategoriaProducto> = ref(new CategoriaProducto());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCategoriaProducto = async categoriaProductoId => {
      try {
        const res = await categoriaProductoService().find(categoriaProductoId);
        categoriaProducto.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.categoriaProductoId) {
      retrieveCategoriaProducto(route.params.categoriaProductoId);
    }

    const initRelationships = () => {};

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      descripcion: {
        maxLength: validations.maxLength('Este campo no puede superar más de 250 caracteres.', 250),
      },
      productos: {},
    };
    const v$ = useVuelidate(validationRules, categoriaProducto as any);
    v$.value.$validate();

    return {
      categoriaProductoService,
      alertService,
      categoriaProducto,
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
      if (this.categoriaProducto.id) {
        this.categoriaProductoService()
          .update(this.categoriaProducto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A CategoriaProducto is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.categoriaProductoService()
          .create(this.categoriaProducto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A CategoriaProducto is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
