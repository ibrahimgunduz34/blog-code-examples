'use strict';

const sinon = require('sinon');
const chai = require('chai');
const chaiAsPromised = require('chai-as-promised');
const sinonChai = require('sinon-chai');
const { Client } = require('../src/client');

const expect = chai.expect;

chai.use(chaiAsPromised);
chai.use(sinonChai);

describe('Unit Tests - Client', () => {
  let sandbox;
  let requestBuilderStub;
  let responseParserStub;
  let httpClientStub;
  let client;

  beforeEach(() => {
    sandbox = sinon.createSandbox();
    requestBuilderStub = { buildGetListRequest: sandbox.stub() }
    responseParserStub = { parseGetListResponse: sandbox.stub() }
    httpClientStub = { request: sandbox.stub() }
    client = new Client(requestBuilderStub, responseParserStub, httpClientStub);
  });

  afterEach(() => {
    sandbox.restore();
  })

  describe('When getList() method is called and the api result consists of one page', () => {
    it('should return a successful result expectedly without making a recursive call', async () => {
      const dummyRequest = {
        url: 'https://example.com',
        method: 'GET',
        params: {
          page: 1
        }
      }

      const dummyResponse = {
        statusCode: 200,
        headers: {
          'Content-Type': 'application/json',
          'x-total-pages': 1,
        },
        data: '[{"key1": "value 1"}, {"key2": "value 2"}]'
      }

      const dummyParsedResponse = [{ key1: "value 1" }, { key2: "value 2" }]

      requestBuilderStub.buildGetListRequest.returns(dummyRequest);
      httpClientStub.request.resolves(dummyResponse);
      responseParserStub.parseGetListResponse.returns(dummyParsedResponse);

      await expect(client.getList()).to.be.fulfilled.and.eventually.deep.equal(dummyParsedResponse);
      expect(requestBuilderStub.buildGetListRequest).to.have.been.calledOnceWith(1);
      expect(httpClientStub.request).to.have.been.calledOnceWith(dummyRequest);
      expect(responseParserStub.parseGetListResponse).to.have.been.calledOnceWith(dummyResponse);
    });
  });

  describe('When getList() method is called and the api result greater than one page', () => {
    it('should return a successful result expectedly with making a recursive call', async () => {
      const firstDummyRequest = {
        url: 'https://example.com',
        method: 'GET',
        params: {
          page: 1
        }
      }

      const secondDummyRequest = {
        url: 'https://example.com',
        method: 'GET',
        params: {
          page: 2
        }
      }

      const firstDummyResponse = {
        statusCode: 200,
        headers: {
          'Content-Type': 'application/json',
          'x-total-pages': 2,
        },
        data: '[{"key1": "value 1"}, {"key2": "value 2"}]'
      }

      const secondDummyResponse = {
        statusCode: 200,
        headers: {
          'Content-Type': 'application/json',
          'x-total-pages': 2,
        },
        data: '[{"key3": "value 3"}, {"key4": "value 4"}]'
      }

      const firstDummyParsedResponse = [{ key1: "value 1" }, { key2: "value 2" }];
      const secondDummyParsedResponse = [{ key3: "value 3" }, { key4: "value 4" }]
      const expectedResult = [...firstDummyParsedResponse, ...secondDummyParsedResponse];

      requestBuilderStub.buildGetListRequest
        .onCall(0).returns(firstDummyRequest)
        .onCall(1).returns(secondDummyRequest);

      httpClientStub.request
        .onCall(0).resolves(firstDummyResponse)
        .onCall(1).resolves(secondDummyResponse);

      responseParserStub.parseGetListResponse
        .onCall(0).returns(firstDummyParsedResponse)
        .onCall(1).returns(secondDummyParsedResponse);

        await expect(client.getList()).to.be.fulfilled.and.eventually.deep.equal(expectedResult);
        
        expect(requestBuilderStub.buildGetListRequest).to.have.been.calledTwice;
        expect(requestBuilderStub.buildGetListRequest.getCall(0)).to.have.been.calledWith(1);
        expect(requestBuilderStub.buildGetListRequest.getCall(1)).to.have.been.calledWith(2);

        expect(httpClientStub.request).to.have.been.calledTwice;
        expect(httpClientStub.request.getCall(0)).to.have.been.calledWith(firstDummyRequest);
        expect(httpClientStub.request.getCall(1)).to.have.been.calledWith(secondDummyRequest);

        expect(responseParserStub.parseGetListResponse).to.have.been.calledTwice;
        expect(responseParserStub.parseGetListResponse.getCall(0)).to.have.been.calledWith(firstDummyResponse);
        expect(responseParserStub.parseGetListResponse.getCall(1)).to.have.been.calledWith(secondDummyResponse);
    });
  });
});