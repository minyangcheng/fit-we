/*global Vue*/

/* weex initialized here, please do not move this line */
const router = require('./router');
const App = require('@/index.vue');
/* eslint-disable no-new */
new Vue(Vue.util.extend({el: '#root', router}, App));

let routeInfo = weex.config.routeInfo;
if (routeInfo && routeInfo.path) {
  router.replace({path: routeInfo.path});
} else {
  router.push('/');
}
