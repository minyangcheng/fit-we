# Fit-We

fit-we是一套weex开发工程化框架，无侵入且扩展性强，可以让你轻松使用weex开发移动项目。框架中内置了一些必要的通用模块，如路由跳转、数据存储、事件通知等等，统一定义了模块书写和调用方式，对外也提供一套扩展模块功能接口。采用多页开发的形式，用webpack进行打包，将生成的文件并入zip包中，让渲染容器从本地快速加载js文件，当然你也可以选择从远程服务器加载js文件。

## 目录

1. [如何使用模块](./document/chapter_1.md)

## 项目结构

.
├── bin  打包脚本
├── config 公共参数配置
├── document 文档
├── fit-library fit-we前端开发库
│   └── bridge
│       └── api
├── output  打包输出
├── platforms 
│   └── android android平台代码
│       ├── app
│       ├── build
│       ├── fit-we fit-we原生端开发库
│       └── gradle
├── src  前端项目代码
│   ├── assets
│   │   ├── img 图片
│   │   └── scss 公共样式
│   ├── components 项目公共组件
│   ├── page 页面
│   └── util 工具类
└── test  测试



