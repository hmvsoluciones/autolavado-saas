import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CompraUpdate from './compra-update.vue';
import CompraService from './compra.service';
import AlertService from '@/shared/alert/alert.service';

import ProveedorService from '@/entities/proveedor/proveedor.service';

type CompraUpdateComponentType = InstanceType<typeof CompraUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const compraSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CompraUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Compra Management Update Component', () => {
    let comp: CompraUpdateComponentType;
    let compraServiceStub: SinonStubbedInstance<CompraService>;

    beforeEach(() => {
      route = {};
      compraServiceStub = sinon.createStubInstance<CompraService>(CompraService);
      compraServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          compraService: () => compraServiceStub,
          proveedorService: () =>
            sinon.createStubInstance<ProveedorService>(ProveedorService, {
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
        const wrapper = shallowMount(CompraUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.compra = compraSample;
        compraServiceStub.update.resolves(compraSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(compraServiceStub.update.calledWith(compraSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        compraServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CompraUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.compra = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(compraServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        compraServiceStub.find.resolves(compraSample);
        compraServiceStub.retrieve.resolves([compraSample]);

        // WHEN
        route = {
          params: {
            compraId: `${compraSample.id}`,
          },
        };
        const wrapper = shallowMount(CompraUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.compra).toMatchObject(compraSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        compraServiceStub.find.resolves(compraSample);
        const wrapper = shallowMount(CompraUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
