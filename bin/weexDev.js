const path = require('path');
const webpack = require('webpack');
const middleware = require('webpack-dev-middleware');
const webpackConfig = require('./weexBuild');
const compiler = webpack(webpackConfig);
var listeneReCompilePlugin = require('./listeneReCompilePlugin');

var http = require('http');
const express = require('express');
const app = express();
var server = http.createServer(app);
var io = require('socket.io')(server);

io.on('connection', function (socket) {
  console.log('**日志**： new client connection hot server');
});

listeneReCompilePlugin.setHotRefresh(function () {
  io.emit('chat', 'refresh');
});

const instance = middleware(compiler, {publicPath: webpackConfig.output.publicPath});
app.use(instance);
app.use(express.static(path.resolve(__dirname, '../')));

server.listen(8888, () => console.log('listening on port 8888 !'));
