import callInner from '../util/callInner';
import {compatibleStringParamsToObject} from '../util/utils';

export default {
  moduleName: '$tool',
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
