# 打包的zip文件原生端怎样使用

## 功能实现思路

### 统一概念
首先解释下三个版本的含义：
1. localVersion：当本地已经成功解压zip包中的内容，该zip包中buildConfig中的version的值
2. assetsVersion：android中assets下bundle.zip中的buildConfig中的version的值
3. downloadVersion: 当去请求服务器自更新接口的时候，如果有新的weex包则将服务器返回的version作为downloadVersion值

### 步骤
1. 比较downloadVersion和assetsVersion，如果downloadVersion不为空且大于assetsVersion，则使用更新接口下载的zip包，否则使用assets中的bundle.zip
2. 加压


