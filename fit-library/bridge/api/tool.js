var callInner = require('../callInner');
var {compatibleStringParamsToObject} = require('../moduleUtil');

module.exports = {
  name: 'tool',
  apis: [
    {
      namespace: 'printLog',
      defaultParams: {
        message: '',
      },
      // runCode(...rest) {
      //   const args = compatibleStringParamsToObject.call(
      //     this,
      //     rest,
      //     'message');
      //   callInner.apply(this, args);
      // },
    }
  ]
}
