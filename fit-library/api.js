var vue = {};
var weex = {};

var proxyApis = {};

function extendModule(moduleName, apiArray) {
  if (!apiArray || !Array.isArray(apiArray) || apiArray.length == 0) {
    return;
  }
  if (!vue[moduleName]) {
    vue[moduleName] = {};
  }
  vue[moduleName].name = moduleName;
  apiArray.forEach((api, index) => {
    extendApi(vue[moduleName], api);
  })
}

function extendApi(module, api) {
  if (!api.namespace) {
    return;
  }
  api.moduleName = module.name;
  var finalSpace = module.name + '.' + api.namespace;
  if (!api.runCode) {
    api.runCode = callInner;
  }
  proxyApis[finalSpace] = new Proxy(api, api.runCode);
  defineApi(module, api, finalSpace);
}

function defineApi(module, api, finalSpace) {
  Object.defineProperty(module, api.namespace, {
    configurable: true,
    enumerable: true,
    get: function proxyGetter() {
      const proxyObj = proxyApis[finalSpace];
      if (proxyObj) {
        return proxyObj.walk();
      }
      console.error('can find ' + api.namespace + ' in ' + module.name);
      return undefined;
    },
    set: function proxySetter() {
    },
  });
}

function Proxy(api, runCode) {
  this.api = api;
  this.runCode = runCode;
}

Proxy.prototype.walk = function walk() {
  return (...rest) => {
    let args = rest;
    args[0] = args[0] || {};
    if (this.api.defaultParams && (args[0] instanceof Object)) {
      Object.keys(this.api.defaultParams).forEach((item) => {
        if (args[0][item] === undefined) {
          args[0][item] = this.api.defaultParams[item];
        }
      });
    }
    let finallyRunCode;
    if (this.runCode) {
      finallyRunCode = this.runCode;
    }
    if (Promise) {
      return finallyRunCode && new Promise((resolve, reject) => {
        args = args.concat([resolve, reject]);
        finallyRunCode.apply(this, args);
      });
    }
    return finallyRunCode && finallyRunCode.apply(this, args);
  };
};

function callInner(options, resolve, reject) {
  var data = Object.assign({}, options);
  data.success = undefined;
  data.error = undefined;
  var success = options.success;
  if (!success) {
    success = function () {
    };
  }
  var error = options.error;
  if (!error) {
    error = function () {
    };
  }
  // var weexModule=weex[(this.api.moduleName)];
  // if(!weexModule){
  //   weexModule[this.api.namespace](data,options.success,options.error);
  // }else{
  //   console.error('weex native can not find '+this.api.moduleName);
  // }
  //todo
  success && success({data: 'success callback'});
  resolve && resolve({data: 'success resolve'});
}

function compatibleStringParamsToObject(args, ...rest) {
  const newArgs = args;
  if (!(newArgs[0] instanceof Object)) {
    const options = {};
    const isPromise = !!Promise;
    const len = newArgs.length;
    const paramsLen = isPromise ? (len - 2) : len;
    for (let i = 0; i < paramsLen; i += 1) {
      if (rest[i] !== undefined) {
        options[rest[i]] = newArgs[i];
      }
    }
    newArgs[0] = options;
    if (Promise) {
      newArgs[1] = newArgs[len - 2];
      newArgs[2] = newArgs[len - 1];
    } else {
      newArgs[1] = undefined;
      newArgs[2] = undefined;
    }
  }
  // 默认参数的处理，因为刚兼容字符串后是没有默认参数的
  if (this.api && this.api.defaultParams && (newArgs[0] instanceof Object)) {
    Object.keys(this.api.defaultParams).forEach((item) => {
      if (newArgs[0][item] === undefined) {
        newArgs[0][item] = this.api.defaultParams[item];
      }
    });
  }
  return newArgs;
}

extendModule('ui', [
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
]);

vue.ui.toast({
  datetime: '2018-03-01',
  success(result) {
    console.log(result)
  },
  error(err) {
    console.log(err)
  }
});

vue.ui.toast({
  datetime: '2018-03-01'
}).then(value => console.log(value))
  .catch(err => console.log(err));

vue.ui.toast('nihaoma');
