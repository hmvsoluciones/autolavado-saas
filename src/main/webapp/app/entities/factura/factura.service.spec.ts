import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import FacturaService from './factura.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Factura } from '@/shared/model/factura.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Factura Service', () => {
    let service: FacturaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new FacturaService();
      currentDate = new Date();
      elemDefault = new Factura(123, 'AAAAAAA', currentDate, 0, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaEmision: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Factura', async () => {
        const returnedFromService = { id: 123, fechaEmision: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { fechaEmision: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Factura', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Factura', async () => {
        const returnedFromService = {
          numero: 'BBBBBB',
          fechaEmision: dayjs(currentDate).format(DATE_FORMAT),
          total: 1,
          activo: true,
          ...elemDefault,
        };

        const expected = { fechaEmision: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Factura', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Factura', async () => {
        const patchObject = { fechaEmision: dayjs(currentDate).format(DATE_FORMAT), activo: true, ...new Factura() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaEmision: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Factura', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Factura', async () => {
        const returnedFromService = {
          numero: 'BBBBBB',
          fechaEmision: dayjs(currentDate).format(DATE_FORMAT),
          total: 1,
          activo: true,
          ...elemDefault,
        };
        const expected = { fechaEmision: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Factura', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Factura', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Factura', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
