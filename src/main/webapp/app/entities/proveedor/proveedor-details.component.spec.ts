import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProveedorDetails from './proveedor-details.vue';
import ProveedorService from './proveedor.service';
import AlertService from '@/shared/alert/alert.service';

type ProveedorDetailsComponentType = InstanceType<typeof ProveedorDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const proveedorSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Proveedor Management Detail Component', () => {
    let proveedorServiceStub: SinonStubbedInstance<ProveedorService>;
    let mountOptions: MountingOptions<ProveedorDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      proveedorServiceStub = sinon.createStubInstance<ProveedorService>(ProveedorService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          proveedorService: () => proveedorServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        proveedorServiceStub.find.resolves(proveedorSample);
        route = {
          params: {
            proveedorId: `${123}`,
          },
        };
        const wrapper = shallowMount(ProveedorDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.proveedor).toMatchObject(proveedorSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        proveedorServiceStub.find.resolves(proveedorSample);
        const wrapper = shallowMount(ProveedorDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
