import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DetalleVentaDetails from './detalle-venta-details.vue';
import DetalleVentaService from './detalle-venta.service';
import AlertService from '@/shared/alert/alert.service';

type DetalleVentaDetailsComponentType = InstanceType<typeof DetalleVentaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const detalleVentaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('DetalleVenta Management Detail Component', () => {
    let detalleVentaServiceStub: SinonStubbedInstance<DetalleVentaService>;
    let mountOptions: MountingOptions<DetalleVentaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      detalleVentaServiceStub = sinon.createStubInstance<DetalleVentaService>(DetalleVentaService);

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
          detalleVentaService: () => detalleVentaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        detalleVentaServiceStub.find.resolves(detalleVentaSample);
        route = {
          params: {
            detalleVentaId: `${123}`,
          },
        };
        const wrapper = shallowMount(DetalleVentaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.detalleVenta).toMatchObject(detalleVentaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        detalleVentaServiceStub.find.resolves(detalleVentaSample);
        const wrapper = shallowMount(DetalleVentaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
