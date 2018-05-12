function isObject(obj) {
  return Object.prototype.toString.call(obj) == '[object Object]';
}

function compatibleStringParamsToObject(args, ...rest) {
  const newArgs = args;
  if (!isObject(newArgs[0])) {
    const options = {};
    for (let i = 0; i < newArgs.length; i += 1) {
      if (rest[i] !== undefined) {
        options[rest[i]] = newArgs[i];
      }
    }
    newArgs[0] = options;
  }
  // 默认参数的处理，因为刚兼容字符串后是没有默认参数的
  if (this.api && this.api.defaultParams && isObject(newArgs[0])) {
    Object.keys(this.api.defaultParams).forEach((item) => {
      if (newArgs[0][item] === undefined) {
        newArgs[0][item] = this.api.defaultParams[item];
      }
    });
  }
  return newArgs;
}

export {
  compatibleStringParamsToObject,
  isObject
}


