import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProductoUpdate from './producto-update.vue';
import ProductoService from './producto.service';
import AlertService from '@/shared/alert/alert.service';

import CategoriaProductoService from '@/entities/categoria-producto/categoria-producto.service';

type ProductoUpdateComponentType = InstanceType<typeof ProductoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const productoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ProductoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Producto Management Update Component', () => {
    let comp: ProductoUpdateComponentType;
    let productoServiceStub: SinonStubbedInstance<ProductoService>;

    beforeEach(() => {
      route = {};
      productoServiceStub = sinon.createStubInstance<ProductoService>(ProductoService);
      productoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          productoService: () => productoServiceStub,
          categoriaProductoService: () =>
            sinon.createStubInstance<CategoriaProductoService>(CategoriaProductoService, {
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
        const wrapper = shallowMount(ProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.producto = productoSample;
        productoServiceStub.update.resolves(productoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productoServiceStub.update.calledWith(productoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        productoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.producto = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        productoServiceStub.find.resolves(productoSample);
        productoServiceStub.retrieve.resolves([productoSample]);

        // WHEN
        route = {
          params: {
            productoId: `${productoSample.id}`,
          },
        };
        const wrapper = shallowMount(ProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.producto).toMatchObject(productoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        productoServiceStub.find.resolves(productoSample);
        const wrapper = shallowMount(ProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
