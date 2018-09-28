var hotRefresh;

function Plugin(options) {
}

Plugin.prototype.setHotRefresh = function (hotMethod) {
  hotRefresh = hotMethod;
}

Plugin.prototype.apply = function (compiler) {
  compiler.plugin("done", function () {
    setTimeout(() => {
      hotRefresh && hotRefresh();
    }, 1000);
  });
};

module.exports = new Plugin();
