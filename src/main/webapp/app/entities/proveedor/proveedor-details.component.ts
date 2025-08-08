import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ProveedorService from './proveedor.service';
import { type IProveedor } from '@/shared/model/proveedor.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProveedorDetails',
  setup() {
    const proveedorService = inject('proveedorService', () => new ProveedorService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const proveedor: Ref<IProveedor> = ref({});

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

    return {
      alertService,
      proveedor,

      previousState,
    };
  },
});
