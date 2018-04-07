var callInner = require('../callInner');
var {compatibleStringParamsToObject} = require('../moduleUtil');

module.exports = {
  name: '$tool',
  apis: [
    {
      namespace: 'printLog',
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, '_');
        callInner.apply(this, args);
      },
    }
  ]
}
