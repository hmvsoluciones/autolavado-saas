import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FacturaDetails from './factura-details.vue';
import FacturaService from './factura.service';
import AlertService from '@/shared/alert/alert.service';

type FacturaDetailsComponentType = InstanceType<typeof FacturaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const facturaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Factura Management Detail Component', () => {
    let facturaServiceStub: SinonStubbedInstance<FacturaService>;
    let mountOptions: MountingOptions<FacturaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      facturaServiceStub = sinon.createStubInstance<FacturaService>(FacturaService);

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
          facturaService: () => facturaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        facturaServiceStub.find.resolves(facturaSample);
        route = {
          params: {
            facturaId: `${123}`,
          },
        };
        const wrapper = shallowMount(FacturaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.factura).toMatchObject(facturaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        facturaServiceStub.find.resolves(facturaSample);
        const wrapper = shallowMount(FacturaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
