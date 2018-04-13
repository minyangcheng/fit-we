# 如何将weex页面打包

## 打包功能实现思路

* 采用webpack将vue文件转换成js文件

1. 扫描`src/page`下面所有以`Page.vue`结尾的文件，并将文件作为入口文件，放入webpack config entry上
2. 扫描`assets/img`下面所有的图片，并将文件作为入口文件，放入webpack config entry上

```javascript
function scanPage() {
  var pageDir = path.resolve(__dirname, '../src/page');
  var pageFiles = [];
  util.findFile(pageDir, new RegExp("Page\\.vue$"), pageFiles);
  pageFiles.forEach((filePath, i) => {
    var name = getPageShortPath(filePath)+'.js';
    if (!entryMap[name]) {
      entryMap[name] = [path.resolve(__dirname,'../fit-library/index.js?entry=true'),filePath + '?entry=true'];
    }
  });
}

function scanImg() {
  var pageDir = path.resolve(__dirname, '../src/assets/img');
  var pageFiles = [];
  util.findFile(pageDir,new RegExp("\\.(png|jpg|gif|jpeg)$"), pageFiles);
  pageFiles.forEach((filePath, i) => {
    var name = getImgShortPath(filePath);
    if (!entryMap[name]) {
      entryMap[name] = filePath + '?entry=true';
    }
  });
}
```

```javascript
module.exports = {
  entry: entryMap,
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
  ]
}
```

* 根据webpack生成的文件和package.json中的信息生成buildConfig.json文件，里面有该项目的版本号、签名信息、weex端项目的git reversion number
```javascript
var buildConfigData = {
  version: npmConfig.version,
  signature: signature(distPath),
  weexReversion: getGitReversionNumber()
}
```

签名信息生成规则：查找page下面所有的js文件，并计算每个文件的md5值，然后相加，再把结果计算一次md值，主要是为了让原生端在解压weex zip包的时候更具约定的规则做签名校验，放置不合法的zip包被压缩

* 通过在package.json中定义一个后置钩子(postbuild)，实现将webpack生成的dist下面的所有文件压缩入zip文件，命名如`bundle-1.0.0-2018-04-10-153521.zip`，放入项目更目录下面的`output`中

```json
{
  "dev": "cross-env NODE_ENV=dev node bin/weexDev.js",
  "build": "cross-env NODE_ENV=prod webpack --progress --config bin/weexBuild.js",
  "postbuild": "node bin/pack.js"
}
```

## 使用

### 打包

执行`npm run build`,即可生成一个新的zip包

### 发布

1. 通过更新应用的方式：将生成的zip包放入android的assets下，命名为bundle.zip中
2. 通过只更新weex端zip包的方式： 将包放到服务器上，原生端通过接口去请求更新接口，判断是否有新包，如果有则将更新包下载到本地使用
