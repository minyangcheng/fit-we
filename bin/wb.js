var WebSocketServer = require('ws').Server,
  wss = new WebSocketServer({ port: 8888 });
wss.on('connection', function (ws) {
  console.log('client connected');
});
