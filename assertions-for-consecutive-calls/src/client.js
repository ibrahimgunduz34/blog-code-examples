'use strict';

const HEADER_TOTAL_PAGES = 'x-total-pages';

function Client(requestBuilder, responseParser, httpClient) {
  this.requestBuilder = requestBuilder;
  this.responseParser = responseParser;
  this.httpClient = httpClient;
}

Client.prototype = {
  getList: async function(page = 1)  {
    const request = this.requestBuilder.buildGetListRequest(page);
    const response = await this.httpClient.request(request);
    const totalPages = response.headers[HEADER_TOTAL_PAGES] || 1;
    const parsedResponse = this.responseParser.parseGetListResponse(response);

    if (totalPages === page) {
      return parsedResponse;
    }

    return parsedResponse.concat(await this.getList(page + 1));
  },
}

module.exports = {
  Client: Client,
}