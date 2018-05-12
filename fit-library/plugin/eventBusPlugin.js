var messageHandlers = {};

var globalEvent = weex.requireModule('globalEvent');
var pageModule = weex.requireModule('$page');

globalEvent.addEventListener("eventbus", function (message) {
  console.log(`eventbus receive message=${message}`);
  let keyArr = Object.keys(messageHandlers);
  keyArr.forEach(function (value, index) {
    if (value === message.type || value.indexOf(message.type, value.length - message.type.length) !== -1) {
      const handler = messageHandlers[value];
      const data = message.data;
      handler && handler(data);
    }
  })
});

var on = function (eventName, handlerFun) {
  if (eventName && handlerFun) {
    var name = weex.config.bundleUrl + '_' + eventName;
    messageHandlers[name] = handlerFun;
  }
}

var off = function (eventName) {
  var name = weex.config.bundleUrl + '_' + eventName;
  delete messageHandlers[name];
}

var post = function (type, data) {
  var success = () => console.log(`post event success type=${type} ,data=${data}`);
  var error = () => console.log(`post event fail type=${type} ,data=${data}`);
  pageModule.postEvent({type, data}, success, error);
}

var plugin = {};
plugin.install = function (Vue, options) {
  Vue.prototype.$event = {on, off, post};
}

export default plugin;
