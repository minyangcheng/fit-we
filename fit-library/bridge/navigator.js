import callInner from '../util/callInner';
import {compatibleStringParamsToObject} from '../util/utils';

export default {
  moduleName: '$navigator',
  apis: [
    {
      namespace: 'setTitle',
      defaultParams: {
        title: '',
        subTitle: '',
        direction: 'bottom',
        clickable: 0,
      },
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'title');
        callInner.apply(this, args);
      },
    },
    {
      namespace: 'show'
    },
    {
      namespace: 'hide'
    },
    {
      namespace: 'showStatusBar'
    },
    {
      namespace: 'hideBackBtn'
    },
    {
      namespace: 'hideStatusBar'
    },
    {
      namespace: 'hideBackButton'
    },
    {
      namespace: 'hookSysBack',
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'success');
        callInner.apply(this, args);
      },
    },
    {
      namespace: 'hookBackBtn',
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'success');
        callInner.apply(this, args);
      },
    },
    {
      namespace: 'setRightBtn',
      defaultParams: {
        text: '',
        imageUrl: '',
        isShow: 1,
        which: 0,
      }
    },
    {
      namespace: 'setRightMenu',
      defaultParams: {
        text: '',
        imageUrl: '',
        iconFilterColor: '',
        titleItems: [],
        iconItems: [],
      },
      runCode(...rest) {
        const newArgs = [].slice.call(rest);
        const newOptions = Object.assign({}, newArgs[0]);

        newOptions.success = () => {
          Vue.prototype.$ui.popWindow.apply(this, rest);
        };

        newArgs[0] = newOptions;
        Vue.prototype.$navigator.setRightBtn.apply(this, newArgs);
      }
    },
    {
      namespace: 'setLeftBtn',
      defaultParams: {
        text: '',
        imageUrl: '',
        isShow: 1,
      }
    }
  ]
}
