<template>
  <scroller>
    <div v-for="(item,index) in items" :key="index" @click="onItemClick(item,index)">
      <text class="item">{{item}}</text>
    </div>
    <image style="margin:100px 0px;width:100px;height:100px;align-self: center" src="fit://assets/img/logo.png"></image>
  </scroller>
</template>

<script>

  export default {
    data() {
      return {
        items: ['navigator模块', 'page模块', 'ui模块', 'tool模块', 'zeus调用外部扩展的功能', 'post事件通知', 'InfoPage页面公共参数信息', 'weex-ui库的使用',
          'InputEditText控件', '测试调试页面'],
      }
    },
    created() {
      this.$tool.printLog('SamplePage created');
      this.$navigator.setTitle({
        title: 'FitWe',
      });
      this.$event.on('testType', (data) => {
        Vue.prototype.$ui.toast(JSON.stringify(data));
      });

      var globalEvent = weex.requireModule('globalEvent');
      globalEvent.addEventListener('onResume', (obj) => {
        this.$tool.printLog('onResume');
      });
      globalEvent.addEventListener('onStop', (obj) => {
        this.$tool.printLog('onStop');
      });
    },
    destroyed(){
      this.$tool.printLog('SamplePage destroyed');
    },
    methods: {
      onItemClick(item, index) {
        switch (item) {
          case 'navigator模块':
            this.$router.open('fit://page/NavigatorPage', '11');
            break;
          case 'page模块':
            this.$router.open('fit://page/RouterPage');
            break;
          case 'tool模块':
            this.$router.open('fit://page/ToolPage');
            break;
          case 'ui模块':
            this.$router.open('fit://page/UiPage');
            break;
          case 'zeus调用外部扩展的功能':
            this.$router.open('fit://page/ZeusPage');
            break;
          case 'post事件通知':
            this.$event.post('testType', {name: 'minych'});
            break;
          case 'InfoPage页面公共参数信息':
            this.$router.open('fit://page/InfoPage');
            break;
          case 'weex-ui库的使用':
            this.$router.open('fit://page/WeexUiPage');
            break;
          case 'InputEditText控件':
            this.$router.open('fit://page/InputEditTextPage');
            break;
          case '测试调试页面':
            // this.$router.open('fit://page/TestPage');
            var navigator = weex.requireModule('navigator')
            navigator.push({
              url: 'http://10.10.12.170:8888/page/InputEditTextPage.js',
              animated: 'false'
            }, event => {
              weex.requireModule('modal').toast({message: 'callback: ' + event})
            })
            break;
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "../assets/scss/values.scss";

  .item {
    width: 750px;
    height: $testValue;
    line-height: 90px;
    background-color: #ffffff;
    color: #666666;
    margin: 5px 0px;
    padding: 0px 15px;
    font-size: 30px;
  }
</style>
