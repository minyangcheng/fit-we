import {isObject} from './utils';
import callInner from './callInner';

var bindObject = Vue.prototype;

var proxyApis = {};

function extendModule({moduleName, apis}) {
  if (!moduleName || !apis || !Array.isArray(apis) || apis.length == 0) {
    return;
  }
  if (!bindObject[moduleName]) {
    bindObject[moduleName] = {};
  }
  bindObject[moduleName].name = moduleName;
  apis.forEach((api, index) => {
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
    if (this.api.defaultParams && isObject(args[0])) {
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
    return finallyRunCode && finallyRunCode.apply(this, args);
  };
};

export default extendModule;


