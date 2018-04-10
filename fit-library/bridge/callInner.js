module.exports = function callInner(options, resolve, reject) {
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
  var moduleName = this.api.moduleName;
  var namespace = this.api.namespace;
  var weexModule = weex.requireModule(moduleName);
  if (weexModule && weexModule[namespace]) {
    weexModule[namespace](data, function (value) {
      success && success(value);
      resolve && resolve(value);
    }, function (err) {
      error && error(err);
      reject && reject(err);
    });
  } else {
    var log = `weex can not find ${moduleName}.${namespace}`;
    console.error(log);
  }
};
