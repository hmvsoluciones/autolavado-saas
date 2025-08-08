import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import VentaUpdate from './venta-update.vue';
import VentaService from './venta.service';
import AlertService from '@/shared/alert/alert.service';

import ClienteService from '@/entities/cliente/cliente.service';

type VentaUpdateComponentType = InstanceType<typeof VentaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const ventaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<VentaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Venta Management Update Component', () => {
    let comp: VentaUpdateComponentType;
    let ventaServiceStub: SinonStubbedInstance<VentaService>;

    beforeEach(() => {
      route = {};
      ventaServiceStub = sinon.createStubInstance<VentaService>(VentaService);
      ventaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          ventaService: () => ventaServiceStub,
          clienteService: () =>
            sinon.createStubInstance<ClienteService>(ClienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(VentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.venta = ventaSample;
        ventaServiceStub.update.resolves(ventaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ventaServiceStub.update.calledWith(ventaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        ventaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(VentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.venta = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ventaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        ventaServiceStub.find.resolves(ventaSample);
        ventaServiceStub.retrieve.resolves([ventaSample]);

        // WHEN
        route = {
          params: {
            ventaId: `${ventaSample.id}`,
          },
        };
        const wrapper = shallowMount(VentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.venta).toMatchObject(ventaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        ventaServiceStub.find.resolves(ventaSample);
        const wrapper = shallowMount(VentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
