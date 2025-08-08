import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CompraDetails from './compra-details.vue';
import CompraService from './compra.service';
import AlertService from '@/shared/alert/alert.service';

type CompraDetailsComponentType = InstanceType<typeof CompraDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const compraSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Compra Management Detail Component', () => {
    let compraServiceStub: SinonStubbedInstance<CompraService>;
    let mountOptions: MountingOptions<CompraDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      compraServiceStub = sinon.createStubInstance<CompraService>(CompraService);

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
          compraService: () => compraServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        compraServiceStub.find.resolves(compraSample);
        route = {
          params: {
            compraId: `${123}`,
          },
        };
        const wrapper = shallowMount(CompraDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.compra).toMatchObject(compraSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        compraServiceStub.find.resolves(compraSample);
        const wrapper = shallowMount(CompraDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
