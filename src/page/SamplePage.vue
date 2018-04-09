<template>
  <div>
    <div v-for="(item,index) in items" :key="index" @click="onItemClick(item,index)">
      <text class="item">{{item}}</text>
    </div>
    <image style="width:500px;height:500px" src="fit://assets/img/logo.png"></image>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        items: ['navigator', 'page', 'ui', 'tool', 'callApi(调用扩展1的功能)'],
      }
    },
    created() {
      let version = process.env.VERSION;
      this.$navigator.setTitle({
        title: 'main',
        subTitle: 'v' + version + '  ' + process.env.NODE_ENV
      });
      console.log(weex.config)
    },
    mounted() {
      this.$event.on('testType', (data) => {
        Vue.prototype.$ui.toast(JSON.stringify(data));
      });
    },
    beforeDestroy() {
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
            this.$event.off('testType');
            break;
          case 'ui':
            this.$page.open('fit://page/UiPage');
            break;
        }
      }
    }
  }
</script>

<style lang="scss" type="text/scss" scoped>
  /*@import "../assets/scss/values.scss";*/

  .item {
    height: 100px;
    line-height: 90px;
    background-color: #ffffff;
    color: #666666;
    margin: 5px 0px;
    padding: 0px 15px;
    font-size: 30px;
  }
</style>
