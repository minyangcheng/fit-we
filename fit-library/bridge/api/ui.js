var callInner = require('../callInner');
var {compatibleStringParamsToObject} = require('../moduleUtil');

module.exports = {
  name: 'ui',
  apis: [
    {
      namespace: 'toast',
      defaultParams: {
        message: '',
      },
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(
          this,
          rest,
          'message');
        callInner.apply(this, args);
      },
    }
  ]
}
