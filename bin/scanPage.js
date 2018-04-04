var path = require('path');
var fs = require('fs');
var entryMap = {};

scanPage();

function scanPage() {
  var pageDir = path.resolve(__dirname, '../src/page');
  var pageFiles = [];
  findPageFile(pageDir, pageFiles);
  pageFiles.forEach((value, i) => {
    var name = getShortPath(value);
    if (!entryMap[name]) {
      entryMap[name] = value + '?entry=true';
    }
  });
}

function getShortPath(str) {
  return str.slice(str.indexOf('page'), str.indexOf('.vue'));
}

function findPageFile(dir, resultFiles) {
  if (fs.existsSync(dir)) {
    var files = fs.readdirSync(dir);
    files.forEach(function (file, index) {
      var curPath = dir + "/" + file;
      if (fs.statSync(curPath).isDirectory()) {
        findPageFile(curPath, resultFiles);
      } else {
        if (/.+page\.vue$/ig.test(curPath)) {
          resultFiles.push(curPath);
        }
      }
    })
  }
}

module.exports = entryMap;
