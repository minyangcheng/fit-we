export default function pageMixin(hybrid) {
  const hybridJs = hybrid;
  const innerUtil = hybridJs.innerUtil;

  hybridJs.extendModule('page', [{
    namespace: 'open',
    os: ['quick'],
    defaultParams: {
      pageUrl: '',
      pageStyle: 1,
      // 横竖屏,默认为1表示竖屏
      orientation: 1,
      // 额外数据
      data: {},
      //如果是内部跳转则需要传入vue实例
      vue: null,
    },
    runCode(...rest) {
      // 兼容字符串形式
      const args = innerUtil.compatibleStringParamsToObject.call(
        this,
        rest,
        'pageUrl',
        'data',
        'vue'
      );
      const options = args[0];
      if (options.vue) {
        options.vue.$router.push({path: options.pageUrl, query: options.data});
      } else {
        // 将额外数据拼接到url中
        options.pageUrl = innerUtil.getFullUrlByParams(options.pageUrl, options.data);
        // 去除无用参数的干扰
        options.data = undefined;

        options.dataFilter = (res) => {
          const newRes = res;
          if (!innerUtil.isObject(newRes.result.resultData)) {
            try {
              newRes.result.resultData = JSON.parse(newRes.result.resultData);
            } catch (e) {
            }
          }
          return newRes;
        };

        args[0] = options;
        hybridJs.callInner.apply(this, args);
      }
    },
  }, {
    namespace: 'openLocal',
    os: ['quick'],
    defaultParams: {
      className: '',
      data: {},
    },
    runCode(...rest) {
      const args = rest;
      const options = args[0];

      options.dataFilter = (res) => {
        const newRes = res;

        if (!innerUtil.isObject(newRes.result.resultData)) {
          try {
            newRes.result.resultData = JSON.parse(newRes.result.resultData);
          } catch (e) {
          }
        }

        return newRes;
      };

      args[0] = options;
      hybridJs.callInner.apply(this, args);
    },
  }, {
    namespace: 'close',
    os: ['quick'],
  }, {
    namespace: 'reload',
    os: ['quick'],
  }, {
    namespace: 'postEvent',
    os: ['quick'],
    defaultParams: {
      type: "",
      data: {},
    }
  }]);
}
