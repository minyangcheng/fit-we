const path = require('path');
const webpack = require('webpack');
const middleware = require('webpack-dev-middleware');
const webpackConfig = require('./weexBuild');
const compiler = webpack(webpackConfig);
const express = require('express');
const app = express();

const instance = middleware(compiler, {publicPath: webpackConfig.output.publicPath});
app.use(instance);
app.use(express.static(path.resolve(__dirname, '../')));

app.listen(8888, () => console.log('Example app listening on port 8888 !'))
