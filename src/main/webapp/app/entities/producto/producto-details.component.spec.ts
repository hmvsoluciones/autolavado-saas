import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProductoDetails from './producto-details.vue';
import ProductoService from './producto.service';
import AlertService from '@/shared/alert/alert.service';

type ProductoDetailsComponentType = InstanceType<typeof ProductoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const productoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Producto Management Detail Component', () => {
    let productoServiceStub: SinonStubbedInstance<ProductoService>;
    let mountOptions: MountingOptions<ProductoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      productoServiceStub = sinon.createStubInstance<ProductoService>(ProductoService);

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
          productoService: () => productoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        productoServiceStub.find.resolves(productoSample);
        route = {
          params: {
            productoId: `${123}`,
          },
        };
        const wrapper = shallowMount(ProductoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.producto).toMatchObject(productoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        productoServiceStub.find.resolves(productoSample);
        const wrapper = shallowMount(ProductoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
