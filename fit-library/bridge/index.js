var callInner = require('./callInner');
var moduleUtil = require('./moduleUtil');
var navigator = require('./api/navigator');
var ui = require('./api/ui');
var tool = require('./api/tool');

var BridgePlugin = {};
BridgePlugin.install = (Vue, options) => {
  moduleUtil.init(Vue.prototype, callInner);
  moduleUtil.extendModule(navigator.name, navigator.apis);
  moduleUtil.extendModule(ui.name, ui.apis);
  moduleUtil.extendModule(tool.name, tool.apis);
  var instance=new Vue();
};

Vue.use(BridgePlugin);
// vue.ui.toast({
//   datetime: '2018-03-01',
//   success(result) {
//     console.log(result)
//   },
//   error(err) {
//     console.log(err)
//   }
// });
//
// vue.ui.toast({
//   datetime: '2018-03-01'
// }).then(value => console.log(value))
//   .catch(err => console.log(err));
//
// vue.navigator.toast('nihaoma');
