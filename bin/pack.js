#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const archiver = require('archiver');
const moment = require('moment');
const crypto = require('crypto');
const shell = require('shelljs');
const logger = require('./logger');

var distPath = path.resolve(__dirname, '../dist');
var tempPath = path.resolve(__dirname, '../dist/temp');
var buildConfigPath = path.resolve(__dirname, '../dist/buildConfig.json')
var packagesPath = path.resolve(__dirname, '../output');
var npmConfigPath = path.resolve(__dirname, '../package.json');

process.on('uncaughtException', function (err) {
  logger.error('打包出现异常\n'+err.stack);
});
process.on('exit', function (code) {
  if (fs.existsSync(distPath)) {
    deleteAll(distPath);
  }
});

if (!fs.existsSync(distPath) || fs.readdirSync(distPath).length < 2) {
  throw new Error('请先生成dist目录');
}

var npmConfig = JSON.parse(fs.readFileSync(npmConfigPath, 'utf-8'));
var nowTime = moment().format('YYYY-MM-DD-HHmmss');
var zipPath = packagesPath + '/bundle-' + npmConfig.version + '-' + nowTime + '.zip';

var buildConfigData = {
  version: npmConfig.version + '.' +(new Date()).getTime(),
  signature: signature(distPath),
  gitRevision: getGitRevisionNumber()
};

deleteAll(tempPath);
writeVersionInfo(buildConfigPath, buildConfigData);
zipDir(distPath, zipPath);

function writeVersionInfo(filePath, data) {
  data = JSON.stringify(data);
  var writerStream = fs.createWriteStream(filePath);
  writerStream.write(data, 'UTF8');
  writerStream.end();
  writerStream.on('finish', function (fileName) {
    logger.info('生成buildConfig成功： '+data);
  });
  writerStream.on('error', function (err) {
    throw new Error('生成buildConfig失败');
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
    logger.info('打包完成:'+outputZipPath);
  });
  output.on('end', function () {
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
        if (/.+\.js$/ig.test(curPath)) {
          resultFiles.push(curPath);
        }
      }
    })
  }

}

function getGitRevisionNumber() {
  var reversion = shell.exec('git rev-parse --short HEAD');
  return reversion.toString().trim();
}
