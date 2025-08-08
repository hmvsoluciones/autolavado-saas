import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CategoriaProductoDetails from './categoria-producto-details.vue';
import CategoriaProductoService from './categoria-producto.service';
import AlertService from '@/shared/alert/alert.service';

type CategoriaProductoDetailsComponentType = InstanceType<typeof CategoriaProductoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const categoriaProductoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CategoriaProducto Management Detail Component', () => {
    let categoriaProductoServiceStub: SinonStubbedInstance<CategoriaProductoService>;
    let mountOptions: MountingOptions<CategoriaProductoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      categoriaProductoServiceStub = sinon.createStubInstance<CategoriaProductoService>(CategoriaProductoService);

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
          categoriaProductoService: () => categoriaProductoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        categoriaProductoServiceStub.find.resolves(categoriaProductoSample);
        route = {
          params: {
            categoriaProductoId: `${123}`,
          },
        };
        const wrapper = shallowMount(CategoriaProductoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.categoriaProducto).toMatchObject(categoriaProductoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        categoriaProductoServiceStub.find.resolves(categoriaProductoSample);
        const wrapper = shallowMount(CategoriaProductoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
