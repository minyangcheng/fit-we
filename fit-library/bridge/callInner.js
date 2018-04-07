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
  var weexModule = weex.requireModule(moduleName);
  if (weexModule) {
    weexModule[this.api.namespace](data, function (value) {
      success && success(value);
      resolve && resolve(value);
    }, function (error) {
      error && error(error);
      reject && reject(error);
    });
  } else {
    console.error('weex native can not find ' + moduleName);
  }
};
