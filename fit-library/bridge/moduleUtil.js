var bindObject = {};
var callInner = function () {
};

var proxyApis = {};

function extendModule(moduleName, apiArray) {
  if (!apiArray || !Array.isArray(apiArray) || apiArray.length == 0) {
    return;
  }
  if (!bindObject[moduleName]) {
    bindObject[moduleName] = {};
  }
  bindObject[moduleName].name = moduleName;
  apiArray.forEach((api, index) => {
    extendApi(bindObject[moduleName], api);
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
    if (this.api.defaultParams && (Object.prototype.toString.call(args[0] )) == '[object Object]') {
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

function compatibleStringParamsToObject(args, ...rest) {
  const newArgs = args;
  if ((Object.prototype.toString.call(newArgs[0] )) != '[object Object]') {
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
  if (this.api && this.api.defaultParams && (Object.prototype.toString.call(newArgs[0] )) == '[object Object]') {
    Object.keys(this.api.defaultParams).forEach((item) => {
      if (newArgs[0][item] === undefined) {
        newArgs[0][item] = this.api.defaultParams[item];
      }
    });
  }
  return newArgs;
}

module.exports = {
  init(bindObj, callInnerFun) {
    bindObject = bindObj;
    callInner = callInnerFun;
  },
  extendModule,
  compatibleStringParamsToObject
};


