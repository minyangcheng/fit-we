function callApi(options) {
  if (!options || !options.module || !options.name) {
    console.log('callApi params must has module and name property')
    return;
  }
  var data = Object.assign({}, options);
  data.success = undefined;
  data.error = undefined;
  data.module = undefined;
  data.name = undefined;
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
    var weexModule = weex.requireModule(options.module);
    if (weexModule && weexModule[options.name]) {
      weexModule[options.name](data, function (value) {
        success && success(value);
        resolve && resolve(value);
      }, function (err) {
        error && error(err);
        reject && reject(err);
      });
    } else {
      var log = `weex can not find ${options.module}.${options.name}`;
      console.error(log);
    }
  });
}

var plugin = {};
plugin.install = function (Vue, options) {
  Vue.prototype.$zeus = {callApi};
}

module.exports = plugin;

