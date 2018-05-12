import callInner from '../util/callInner';
import {compatibleStringParamsToObject} from '../util/utils';

export default {
  moduleName: '$ui',
  apis: [
    {
      namespace: 'toast',
      defaultParams: {
        message: '',
      },
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'message');
        callInner.apply(this, args);
      },
    },
    {
      namespace: 'alert',
      defaultParams: {
        title: '',
        message: '',
        buttonLabels: ['确定'],
        cancelable: 1,
      },
    },
    {
      namespace: 'actionSheet',
      defaultParams: {
        items: [],
        cancelBtnName: '取消',
        cancelable: 1,
      }
    },
    {
      namespace: 'popWindow',
      defaultParams: {
        titleItems: [],
        iconItems: [],
        iconFilterColor: '',
      }
    },
    {
      namespace: 'pickDate',
      defaultParams: {
        title: '请选择日期',
        datetime: '',
        dateFormat: 'yyyy-MM-dd'
      },
    },
    {
      namespace: 'pickMonth',
      defaultParams: {
        title: '请选择日期1',
        datetime: '',
        dateFormat: 'yyyy-MM'
      },
    },
    {
      namespace: 'pickTime',
      defaultParams: {
        title: '请选择时间',
        time: '',
        timeFormat: 'HH:mm'
      },
    },
    {
      namespace: 'pickDateTime',
      defaultParams: {
        title1: '',
        title2: '',
        //格式为 yyyy-MM-dd HH:mm
        datetime: '',
      },
    },
    {
      namespace: 'showLoadingDialog',
      defaultParams: {
        message: '',
        cancelable: 1,
      },
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'message');
        callInner.apply(this, args);
      }
    },
    {
      namespace: 'closeLoadingDialog'
    }
  ]
}
