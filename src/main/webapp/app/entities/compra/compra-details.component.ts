import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import CompraService from './compra.service';
import { type ICompra } from '@/shared/model/compra.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CompraDetails',
  setup() {
    const compraService = inject('compraService', () => new CompraService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const compra: Ref<ICompra> = ref({});

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

    return {
      alertService,
      compra,

      previousState,
    };
  },
});
