import callInner from '../util/callInner';

var plugin = {};
plugin.install = function (Vue, options) {
  Vue.prototype.$zeus = {callApi: callInner};
}

export default plugin;

