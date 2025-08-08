import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import CategoriaProductoService from './categoria-producto.service';
import { type ICategoriaProducto } from '@/shared/model/categoria-producto.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CategoriaProductoDetails',
  setup() {
    const categoriaProductoService = inject('categoriaProductoService', () => new CategoriaProductoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const categoriaProducto: Ref<ICategoriaProducto> = ref({});

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

    return {
      alertService,
      categoriaProducto,

      previousState,
    };
  },
});
