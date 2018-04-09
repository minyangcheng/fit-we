var path = require('path');
var webpack = require('webpack');
var CleanWebpackPlugin = require('clean-webpack-plugin');
var entryMap = require('./scanResource');
var listeneReCompilePlugin = require('./listeneReCompilePlugin');

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
        use: [
          {
            loader: 'weex-loader',
            options: {
              stylus: 'vue-style-loader!css-loader!stylus-loader!sass-loader',
              styl: 'vue-style-loader!css-loader!stylus-loader!sass-loader'
            }
          }
        ],
        include,
        exclude,
      },
      {
        test: /\.js$/,
        use: [
          {
            loader: 'babel-loader'
          }
        ],
        include,
        exclude,
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
      '@': resolve('src'),
      'Zeus': resolve('fit-library')
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
  ]
}

if (process.env.NODE_ENV === 'dev') {
  module.exports.plugins = (module.exports.plugins || []).concat([
    listeneReCompilePlugin,
    new webpack.DefinePlugin({
      'process.env': require('../config/dev.env')
    })
  ])
} else if (process.env.NODE_ENV === 'prod') {
  // module.exports.devtool = '#source-map'
  module.exports.plugins = (module.exports.plugins || []).concat([
    new webpack.DefinePlugin({
      'process.env': require('../config/prod.env')
    }),
  ])
}
