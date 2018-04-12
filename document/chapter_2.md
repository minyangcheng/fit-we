# 如何使用事件通知

## 事件通知功能实现思路

### weex端

实现一个类似于事件中心功能，可以订阅方法和取消订阅方法，由于项目采用多页面开发的原因，因此两个页面之间还需要借助原生端来进行通信

```javascript
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

module.exports = plugin;
```

### 原生端

通过weex先前注入module接受事件，然后在将事件通过原生端的事件总线功能转发到每个页面，每个页面接受到后，会通过weex的golbalevent转发到定义weex端定义的事件总线

```
public void onEvent(FitEvent event) {
    if (mWXSDKInstance != null) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", event.type);
        map.put("data", event.data);
        mWXSDKInstance.fireGlobalEventCallback("eventbus", map);
    }
}
```

**原生端如果需要接受weex页面发布的通知，只需要订阅FitEvent即可**

## 使用

### 订阅

```javascript
this.$event.on('getUserName', (data) => {
        Vue.prototype.$ui.toast(JSON.stringify(data));
      });
```
### 取消订阅

```javascript
this.$event.off('getUserName');
```

### 发布事件

```javascript
this.$event.post('getUserName', {name: 'minych'});
```
