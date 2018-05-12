<template>
  <div style="align-items: center;">
    <div v-for="(item,index) in items" :key="index" @click="onItemClick(item,index)">
      <text class="item">{{item}}</text>
    </div>
    <image style="margin:100px 0px;width:100px;height:100px" src="fit://assets/img/logo.png"></image>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        items: ['navigator', 'page', 'ui', 'tool', 'zeus(调用外部扩展的功能)','post(事件通知)','InfoPage(各个参数信息)','WeexUiPage',
                  'TestPage'],
      }
    },
    created() {
      let version = process.env.VERSION;
      this.$navigator.setTitle({
        title: 'main',
        subTitle: 'v' + version + '  '
      });
    },
    mounted() {
      this.$event.on('testType', (data) => {
        Vue.prototype.$ui.toast(JSON.stringify(data));
      });
    },
    beforeDestroy() {
      this.$event.off('testType');
    },
    methods: {
      onItemClick(item, index) {
        switch (item) {
          case 'navigator':
            this.$page.open('fit://page/NavigatorPage');
            break;
          case 'page':
            this.$page.open('fit://page/ActivityPage');
            break;
          case 'tool':
            this.$page.open('fit://page/ToolPage');
            break;
          case 'ui':
            this.$page.open('fit://page/UiPage');
            break;
          case 'zeus(调用外部扩展的功能)':
            this.$page.open('fit://page/ZeusPage');
            break;
          case 'post(事件通知)':
            this.$event.post('testType', {name: 'minych'});
            break;
          case 'InfoPage(各个参数信息)':
            this.$page.open('fit://page/InfoPage');
            break;
          case 'WeexUiPage':
            this.$page.open('fit://page/WeexUiPage');
            break;
          case 'TestPage':
            this.$page.open('fit://page/TestPage');
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
