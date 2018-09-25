import callInner from '../util/callInner';
import {compatibleStringParamsToObject} from '../util/utils';

export default {
  moduleName: '$router',
  apis: [
    {
      namespace: 'open',
      defaultParams: {
        showNavigationBar: true,
        screenOrientation: 1,
        pageUri: '',
        title: '',
        showBackBtn: true,
        data: {},
      },
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'pageUri', 'title');
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
      namespace: 'getRouteParams',
      runCode(...rest) {
        return weex.config.routeInfo.data;
      }
    },
    {
      namespace: 'getRouteInfo',
      runCode(...rest) {
        return weex.config.routeInfo;
      }
    }
  ]
}
