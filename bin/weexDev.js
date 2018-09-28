const path = require('path');
const webpack = require('webpack');
const middleware = require('webpack-dev-middleware');
const webpackConfig = require('./weexBuild');
const compiler = webpack(webpackConfig);
const listenReCompilePlugin = require('./listenReCompilePlugin');
const logger = require('./logger');

var http = require('http');
const express = require('express');
const app = express();
var server = http.createServer(app);
var io = require('socket.io')(server);
var count = 0;

io.on('connection', function (socket) {
  logger.info('ip ' + socket.handshake.address + ' has been connect debug server');
});

listenReCompilePlugin.setHotRefresh(function () {
  io.emit('chat', 'refresh');
  logger.info('debug server has been send refresh event to client ： ' + (++count));
});

const instance = middleware(compiler, {publicPath: webpackConfig.output.publicPath});
app.use(instance);
app.use(express.static(path.resolve(__dirname, '../')));

server.listen(8888, () => logger.info('debug server start , listening on port 8888 !'));
