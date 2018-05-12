import callInner from '../util/callInner';
import {compatibleStringParamsToObject} from '../util/utils';

export default {
  moduleName: '$page',
  apis: [
    {
      namespace: 'open',
      defaultParams: {
        pageStyle: 1,
        orientation: 1,
        pagePath: '',
        title: '',
        showBackBtn: true,
        data: {},
      },
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'pagePath');
        callInner.apply(this, args);
      },
    },
    {
      namespace: 'openLocal',
      defaultParams: {
        className: '',
        data: {},
      }
    },
    {
      namespace: 'close',
    },
    {
      namespace: 'reload',
    },
    {
      namespace: 'postEvent',
      defaultParams: {
        type: "",
        data: {},
      }
    }

  ]
}
