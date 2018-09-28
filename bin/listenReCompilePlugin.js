var hotRefresh;

function Plugin(options) {
}

Plugin.prototype.setHotRefresh = function (hotMethod) {
  hotRefresh = throttle(hotMethod, 1000);
}

Plugin.prototype.apply = function (compiler) {
  compiler.plugin("done", function () {
    setTimeout(() => {
      hotRefresh && hotRefresh();
    }, 1000);
  });
}

function throttle(method, delay) {
  var timer = null;
  return function () {
    var context = this, args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      method.apply(context, args);
    }, delay);
  }
}

module.exports = new Plugin();
