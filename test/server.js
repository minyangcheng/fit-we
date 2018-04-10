var path = require('path');
var express = require('express');
var app = express();

app.use('/static', express.static(__dirname));

app.get('/checkWeexUpdate', function (req, res) {
  var version = '1.0.2';
  console.log('req version='+req.query.version+'  now version='+version)
  if (version > req.query.version) {
    res.send({code: 1000, md5: 'asfas', dist: 'http://10.10.12.151:8889/static/bundle.zip', version: version});
  } else {
    res.send({code: 0});
  }
});

app.listen(8889, function () {
  console.log('listening at port 8889');
});
