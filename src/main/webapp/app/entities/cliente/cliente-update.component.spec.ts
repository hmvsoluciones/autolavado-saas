import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ClienteUpdate from './cliente-update.vue';
import ClienteService from './cliente.service';
import AlertService from '@/shared/alert/alert.service';

type ClienteUpdateComponentType = InstanceType<typeof ClienteUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const clienteSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ClienteUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Cliente Management Update Component', () => {
    let comp: ClienteUpdateComponentType;
    let clienteServiceStub: SinonStubbedInstance<ClienteService>;

    beforeEach(() => {
      route = {};
      clienteServiceStub = sinon.createStubInstance<ClienteService>(ClienteService);
      clienteServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          clienteService: () => clienteServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ClienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cliente = clienteSample;
        clienteServiceStub.update.resolves(clienteSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(clienteServiceStub.update.calledWith(clienteSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        clienteServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ClienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cliente = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(clienteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        clienteServiceStub.find.resolves(clienteSample);
        clienteServiceStub.retrieve.resolves([clienteSample]);

        // WHEN
        route = {
          params: {
            clienteId: `${clienteSample.id}`,
          },
        };
        const wrapper = shallowMount(ClienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cliente).toMatchObject(clienteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        clienteServiceStub.find.resolves(clienteSample);
        const wrapper = shallowMount(ClienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
