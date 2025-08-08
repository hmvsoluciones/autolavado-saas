import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProveedorUpdate from './proveedor-update.vue';
import ProveedorService from './proveedor.service';
import AlertService from '@/shared/alert/alert.service';

type ProveedorUpdateComponentType = InstanceType<typeof ProveedorUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const proveedorSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ProveedorUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Proveedor Management Update Component', () => {
    let comp: ProveedorUpdateComponentType;
    let proveedorServiceStub: SinonStubbedInstance<ProveedorService>;

    beforeEach(() => {
      route = {};
      proveedorServiceStub = sinon.createStubInstance<ProveedorService>(ProveedorService);
      proveedorServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          proveedorService: () => proveedorServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ProveedorUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.proveedor = proveedorSample;
        proveedorServiceStub.update.resolves(proveedorSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(proveedorServiceStub.update.calledWith(proveedorSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        proveedorServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ProveedorUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.proveedor = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(proveedorServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        proveedorServiceStub.find.resolves(proveedorSample);
        proveedorServiceStub.retrieve.resolves([proveedorSample]);

        // WHEN
        route = {
          params: {
            proveedorId: `${proveedorSample.id}`,
          },
        };
        const wrapper = shallowMount(ProveedorUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.proveedor).toMatchObject(proveedorSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        proveedorServiceStub.find.resolves(proveedorSample);
        const wrapper = shallowMount(ProveedorUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
