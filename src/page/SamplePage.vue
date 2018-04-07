<template>
  <div>
    <div v-for="(item,index) in items" :key="index" @click="onItemClick(item,index)">
      <text class="item">{{item}}</text>
    </div>
  </div>
</template>

<script>

  import index from './../../fit-library/index'

  export default {
    data() {
      return {
        items: ['navigator', 'page', 'ui', 'tool', 'callApi(调用扩展的功能)'],
      }
    },
    created() {
      let version = process.env.VERSION;
      this.$navigator.setTitle({
        title: 'main',
        subTitle: 'v' + version
      });
      console.log(weex)
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

<style scoped>
  .item {
    height: 90px;
    line-height: 90px;
    background-color: #ffffff;
    color: #666666;
    margin: 5px 0px;
    padding: 0px 15px;
    font-size: 30px;
  }
</style>
