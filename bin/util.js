var path=require('path');
var fs = require('fs');

module.exports = {

  findFile(dir, reg, resultFiles) {
    if (fs.existsSync(dir)) {
      var files = fs.readdirSync(dir);
      files.forEach(function (file, index) {
        var curPath = dir + "/" + file;
        if (fs.statSync(curPath).isDirectory()) {
          findPageFile(curPath, regArr, resultFiles);
        } else {
          if (reg.test(curPath)) {
            resultFiles.push(curPath);
          }
        }
      })
    }
  }

}
