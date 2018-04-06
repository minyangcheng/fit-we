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
  // var weexModule=weex[(this.api.moduleName)];
  // if(!weexModule){
  //   weexModule[this.api.namespace](data,options.success,options.error);
  // }else{
  //   console.error('weex native can not find '+this.api.moduleName);
  // }
  //todo
  success && success({data: 'success callback'});
  resolve && resolve({data: 'success resolve'});
};
