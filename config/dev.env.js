const prodConfig = require('./prod.config')

module.exports = Object.assign(prodConfig,{
  NODE_ENV:'dev'
});
