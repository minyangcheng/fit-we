var path = require('path');
var util= require('./util');
var entryMap = {};

scanPage();
scanImg();

function scanPage() {
  var pageDir = path.resolve(__dirname, '../src/page');
  var pageFiles = [];
  util.findFile(pageDir, new RegExp("Page\\.vue$"), pageFiles);
  pageFiles.forEach((value, i) => {
    var name = getPageShortPath(value);
    if (!entryMap[name]) {
      entryMap[name] = [path.resolve(__dirname,'../fit-library/index.js?entry=true'),value + '?entry=true'];
    }
  });
}

function scanImg() {
  var pageDir = path.resolve(__dirname, '../src/assets/img');
  var pageFiles = [];
  util.findFile(pageDir,new RegExp("\\.(png|jpg|gif|jpeg)$"), pageFiles);
  pageFiles.forEach((value, i) => {
    var name = getImgShortPath(value);
    if (!entryMap[name]) {
      entryMap[name] = value + '?entry=true';
    }
  });
}

function getPageShortPath(str) {
  return str.slice(str.indexOf('page/'), str.indexOf('.vue'));
}

function getImgShortPath(str) {
  return 'temp/'+str.slice(str.lastIndexOf('/')+1);
}

module.exports = entryMap;
