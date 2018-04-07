var path = require('path');
var webpack = require('webpack');
var CleanWebpackPlugin = require('clean-webpack-plugin');
var entryMap = require('./scanPage');

function resolve(dir) {
  return path.join(__dirname, '../' + dir);
}

var include = resolve('src');
var exclude = resolve('node_modules');

module.exports = {
  entry: entryMap,
  output: {
    path: resolve('dist'),
    publicPath: '',
    filename: '[name].js',
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'weex-loader',
        include,
        exclude,
        options: {
          stylus: 'vue-style-loader!css-loader!stylus-loader',
          styl: 'vue-style-loader!css-loader!stylus-loader'
        }
      },
      {
        test: /\.js$/,
        include,
        exclude,
        loader: 'babel-loader',
      },
      {
        test: /\.(png|jpg|gif)$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[name].[ext]',
              outputPath: 'assets',
              publicPath: '../',
            }
          }
        ]
      }
    ]
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      '@': resolve('src')
    }
  },
  devtool: '#eval-source-map',
  plugins: [
    new webpack.optimize.ModuleConcatenationPlugin(),
    new webpack.BannerPlugin({
      banner: '// { "framework": "Vue" }\n',
      raw: true
    }),
    new CleanWebpackPlugin('dist', {root: resolve('')}),
    new webpack.DefinePlugin({
      'process.env': require('../config/dev.env')
    }),
  ]
}
