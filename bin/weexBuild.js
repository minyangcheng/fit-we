var path = require('path');
var webpack = require('webpack');
var CleanWebpackPlugin = require('clean-webpack-plugin');
var scanResource = require('./scanResource');
var listeneReCompilePlugin = require('./listeneReCompilePlugin');

function resolve(dir) {
  return path.join(__dirname, '../' + dir);
}

module.exports = {
  entry: () => new Promise(((resolve) => resolve(scanResource()))),
  output: {
    path: resolve('dist'),
    publicPath: '',
    filename: '[name]',
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        use: [
          {
            loader: 'weex-loader',
            options: {
              loaders: {
                sass: [{loader: 'sass-loader', options: {indentedSyntax: true, sourceMap: false}}],
                scss: [{loader: 'sass-loader', options: {sourceMap: false}}],
                stylus: [{loader: 'stylus-loader', options: {sourceMap: false}}],
                styl: [{loader: 'stylus-loader', options: {sourceMap: false}}]
              }
            }
          },
        ],
      },
      {
        test: /\.js$/,
        use: [
          {
            loader: 'babel-loader'
          }
        ],
      },
      {
        test: /\.(png|jpg|gif|jpeg)$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[name].[ext]',
              outputPath: 'assets/img',
              publicPath: '../assets/img',
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
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false,
        drop_console: true,
        collapse_vars: true,
        reduce_vars: true,
      },
      beautify: false,
      comments: /{ "framework": "Vue" }/,
      sourceMap: false,
    })
  ]
}

if (process.env.NODE_ENV === 'dev') {
  module.exports.plugins = (module.exports.plugins || []).concat([
    listeneReCompilePlugin,
    new webpack.DefinePlugin({
      'process.env': require('../config/dev.env')
    }),
  ])
} else if (process.env.NODE_ENV === 'prod') {
  module.exports.plugins = (module.exports.plugins || []).concat([
    new webpack.DefinePlugin({
      'process.env': require('../config/prod.env')
    }),
  ])
}
