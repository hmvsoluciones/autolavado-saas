import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ProductoService from './producto.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CategoriaProductoService from '@/entities/categoria-producto/categoria-producto.service';
import { type ICategoriaProducto } from '@/shared/model/categoria-producto.model';
import { type IProducto, Producto } from '@/shared/model/producto.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProductoUpdate',
  setup() {
    const productoService = inject('productoService', () => new ProductoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const producto: Ref<IProducto> = ref(new Producto());

    const categoriaProductoService = inject('categoriaProductoService', () => new CategoriaProductoService());

    const categoriaProductos: Ref<ICategoriaProducto[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveProducto = async productoId => {
      try {
        const res = await productoService().find(productoId);
        producto.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.productoId) {
      retrieveProducto(route.params.productoId);
    }

    const initRelationships = () => {
      categoriaProductoService()
        .retrieve()
        .then(res => {
          categoriaProductos.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 50 caracteres.', 50),
      },
      descripcion: {
        maxLength: validations.maxLength('Este campo no puede superar más de 500 caracteres.', 500),
      },
      precio: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      cantidad: {
        required: validations.required('Este campo es obligatorio.'),
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      fechaActualizacion: {
        required: validations.required('Este campo es obligatorio.'),
      },
      activo: {},
      categoriaProducto: {},
      detalleVentas: {},
    };
    const v$ = useVuelidate(validationRules, producto as any);
    v$.value.$validate();

    return {
      productoService,
      alertService,
      producto,
      previousState,
      isSaving,
      currentLanguage,
      categoriaProductos,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.producto.id) {
        this.productoService()
          .update(this.producto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Producto is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.productoService()
          .create(this.producto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Producto is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
