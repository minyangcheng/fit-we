'use strict'

let version=process.env.npm_package_version
version='"'+version+'"';

module.exports = {
  NODE_ENV: '"production"',
  VERSION:version
}
