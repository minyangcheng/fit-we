var callInner = require('./callInner');
var moduleUtil = require('./moduleUtil');
var navigator = require('./api/navigator');
var ui = require('./api/ui');
var tool = require('./api/tool');
var page = require('./api/page');

var BridgePlugin = {};
BridgePlugin.install = (Vue, options) => {
  moduleUtil.init(Vue.prototype, callInner);
  moduleUtil.extendModule(navigator.name, navigator.apis);
  moduleUtil.extendModule(ui.name, ui.apis);
  moduleUtil.extendModule(tool.name, tool.apis);
  moduleUtil.extendModule(page.name, page.apis);
};

module.exports = BridgePlugin;
