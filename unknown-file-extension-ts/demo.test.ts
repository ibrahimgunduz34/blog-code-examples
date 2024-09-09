import { expect } from 'chai';

describe('Example test case', function () {
  it('should complete successfully', function () {
    const var1 = 1;
		const var2 = 2;
		const total = var1 + var2;
		const expectedValue = 3;
		expect(total).to.eql(expectedValue);
  })
})
