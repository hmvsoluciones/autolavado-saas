import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CategoriaProductoUpdate from './categoria-producto-update.vue';
import CategoriaProductoService from './categoria-producto.service';
import AlertService from '@/shared/alert/alert.service';

type CategoriaProductoUpdateComponentType = InstanceType<typeof CategoriaProductoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const categoriaProductoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CategoriaProductoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CategoriaProducto Management Update Component', () => {
    let comp: CategoriaProductoUpdateComponentType;
    let categoriaProductoServiceStub: SinonStubbedInstance<CategoriaProductoService>;

    beforeEach(() => {
      route = {};
      categoriaProductoServiceStub = sinon.createStubInstance<CategoriaProductoService>(CategoriaProductoService);
      categoriaProductoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          categoriaProductoService: () => categoriaProductoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CategoriaProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.categoriaProducto = categoriaProductoSample;
        categoriaProductoServiceStub.update.resolves(categoriaProductoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(categoriaProductoServiceStub.update.calledWith(categoriaProductoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        categoriaProductoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CategoriaProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.categoriaProducto = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(categoriaProductoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        categoriaProductoServiceStub.find.resolves(categoriaProductoSample);
        categoriaProductoServiceStub.retrieve.resolves([categoriaProductoSample]);

        // WHEN
        route = {
          params: {
            categoriaProductoId: `${categoriaProductoSample.id}`,
          },
        };
        const wrapper = shallowMount(CategoriaProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.categoriaProducto).toMatchObject(categoriaProductoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        categoriaProductoServiceStub.find.resolves(categoriaProductoSample);
        const wrapper = shallowMount(CategoriaProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
