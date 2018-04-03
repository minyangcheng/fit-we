#!/usr/bin/env node

var fs = require('fs');
var path = require('path');
var archiver = require('archiver');
var moment = require('moment');
var crypto = require('crypto');

var distPath = path.resolve(__dirname, '../dist');
var buildConfigPath = path.resolve(__dirname, '../dist/buildConfig.json')
var packagesPath = path.resolve(__dirname, '../output');
var npmConfigPath = path.resolve(__dirname, '../package.json');

process.on('uncaughtException', function (err) {
  console.error('捕获到异常-->');
  console.error(err)
});
process.on('exit', function (code) {
  if(fs.existsSync(distPath)){
    deleteAll(distPath);
    console.log('delete %s finished', distPath);
  }
});

if(!fs.existsSync(distPath)||fs.readdirSync(distPath).length<2){
  throw new Error('请先生成vue dist包');
}

var npmConfig = JSON.parse(fs.readFileSync(npmConfigPath, 'utf-8'));
var nowTime = moment().format('YYYY-MM-DD-HHmmss');
var zipPath = packagesPath + '/bundle-' + npmConfig.version + '-' + nowTime + '.zip';

var buildConfigData = {version: npmConfig.version, signature: signature(distPath)};

writeVersionInfo(buildConfigPath, buildConfigData);
zipDir(distPath, zipPath);

function writeVersionInfo(filePath, data) {
  data = JSON.stringify(data);
  var writerStream = fs.createWriteStream(filePath);
  writerStream.write(data, 'UTF8');
  writerStream.end();
  writerStream.on('finish', function (fileName) {
    console.log(filePath + " create finish");
  });
  writerStream.on('error', function (err) {
    console.log(err.stack);
  });
}

function zipDir(intputPath, outputZipPath) {
  var dir = path.dirname(outputZipPath);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir);
  }
  var output = fs.createWriteStream(outputZipPath);
  var archive = archiver('zip', {
    zlib: {level: 9}
  });
  output.on('close', function () {
    console.log(archive.pointer() + ' total bytes');
    console.log('archiver has been finalized and the output file descriptor has closed.');
  });
  output.on('end', function () {
    console.log('Data has been drained');
  });
  archive.on('error', function (err) {
    throw err;
  });
  archive.pipe(output);
  archive.directory(intputPath, false);
  archive.finalize();
}

function deleteAll(path) {
  var files = [];
  if (fs.existsSync(path)) {
    files = fs.readdirSync(path);
    files.forEach(function (file, index) {
      var curPath = path + "/" + file;
      if (fs.statSync(curPath).isDirectory()) { // recurse
        deleteAll(curPath);
      } else { // delete file
        fs.unlinkSync(curPath);
      }
    });
    fs.rmdirSync(path);
  }
};

function signature(dir) {
  var files = [];
  findNeedSignatureFiles(dir, files);
  files.sort(function (var1, var2) {
    return var1 > var2;
  });

  var allMd5 = '';
  files.forEach(function (file, index) {
    var md5 = mdFile(file);
    if (md5) {
      allMd5 += md5;
    }
  })
  return mdStr(allMd5);
}

function mdFile(path) {
  var data = fs.readFileSync(path);
  var hash = crypto.createHash('md5');
  var md5 = hash.update(data).digest('hex');
  // console.log('path=%s,md5=%s', path, md5);
  return md5;
}

function mdStr(data) {
  var hash = crypto.createHash('md5');
  var md5 = hash.update(data).digest('hex');
  return md5;
}

function findNeedSignatureFiles(dir, resultFiles) {
  if (fs.existsSync(dir)) {
    var files = fs.readdirSync(dir);
    files.forEach(function (file, index) {
      var curPath = dir + "/" + file;
      if (fs.statSync(curPath).isDirectory()) { // recurse
        findNeedSignatureFiles(curPath, resultFiles);
      } else {
        if (/.+\.js$/ig.test(curPath) || /.+\.css$/ig.test(curPath) || /.+\.html$/ig.test(curPath)) {
          resultFiles.push(curPath);
        }
      }
    })
  }

}
