import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import VentaService from './venta.service';
import { type IVenta } from '@/shared/model/venta.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'VentaDetails',
  setup() {
    const ventaService = inject('ventaService', () => new VentaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const venta: Ref<IVenta> = ref({});

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

    return {
      alertService,
      venta,

      previousState,
    };
  },
});
