var colors = require('colors/safe');

module.exports = {
  debug(message) {
    console.log(colors.green('[ FitWe ] ' + message));
  },
  info(message) {
    console.log(colors.blue('[ FitWe ] ' + message));
  },
  error(message) {
    console.log(colors.red('[ FitWe ] ' + message));
  }
}
