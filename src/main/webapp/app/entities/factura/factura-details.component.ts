import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import FacturaService from './factura.service';
import { type IFactura } from '@/shared/model/factura.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FacturaDetails',
  setup() {
    const facturaService = inject('facturaService', () => new FacturaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const factura: Ref<IFactura> = ref({});

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

    return {
      alertService,
      factura,

      previousState,
    };
  },
});
