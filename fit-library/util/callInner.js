function callInner(options) {
  var moduleName = options.moduleName || this.api.moduleName;
  var namespace = options.namespace || this.api.namespace;
  if (!options || !moduleName || !namespace) {
    console.log('callInner params must has moduleName and namespace property')
    return;
  }
  var data = Object.assign({}, options);
  data.success = undefined;
  data.error = undefined;
  data.moduleName = undefined;
  data.namespace = undefined;
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
  return new Promise((resolve, reject) => {
    var weexModule = weex.requireModule(moduleName);
    if (weexModule && weexModule[namespace]) {
      weexModule[namespace](data, function (value) {
        success && success(value);
        resolve(value);
      }, function (err) {
        error && error(err);
        reject(err);
      });
    } else {
      var log = `weex can not find ${moduleName}.${namespace}`;
      console.error(log);
    }
  });
}

export default callInner;

