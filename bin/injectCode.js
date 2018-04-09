var path=require('path');
var fs=require('fs');
var util=require('./util');

var pageDir = path.resolve(__dirname, '../src/page');
var pageFiles = [];
util.findFile(pageDir, new RegExp("Page\\.vue$"), pageFiles);

pageFiles.forEach((filePath)=>{
  var content=fs.readFileSync(filePath);

})
