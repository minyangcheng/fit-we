<template>
  <div>
    <div v-for="(item,index) in items" :key="index" @click="onItemClick(item,index)">
      <text class="item" :title="item" is-link></text>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        items: ['open(在当前页面内部跳转)', 'open(在另一个activity页面打开项目内页面)', 'open(在另一个activity页面打开项目外页面)', 'openLocal', 'close', 'reload', 'postEvent'],
      }
    },
    created() {
    },
    methods: {
      onItemClick(item, index) {
        switch (item) {
          case 'open(在当前页面内部跳转)':
            hybrid.page.open({
              pageUrl: '/HttpPage',
              data: {name: 'minych', age: 12},
              vue: this
            });
            break;
          case 'open(在另一个activity页面打开项目内页面)':
            hybrid.page.open({
              pageUrl: '/HttpPage',
              data: {name: 'minych', age: 12},
            });
            break;
          case 'open(在另一个activity页面打开项目外页面)':
            hybrid.page.open({
              pageUrl: 'http://www.baidu.com',
              data: {name: 'minych', age: 12},
            });
            break;
          case 'openLocal':
            hybrid.page.openLocal({
              className: 'com.min.hybrid.sample.LocalActivity',
              data: {name: 'minych', age: 12},
            });
            break;
          case 'close':
            hybrid.page.close();
            break;
          case 'reload':
            hybrid.page.reload();
            break;
          case 'postEvent':
            hybrid.page.postEvent({
              type: 'refreshType',
              data: {'name': 'min', 'age': 123}
            });
            break;
        }
      }
    }
  }
</script>

<style scoped>

  .item{
    height: 90px;
    line-height: 90px;
    background-color: #ffffff;
    color: #666666;
    margin: 5px 0px;
    padding: 0px 15px;
    font-size: 30px;
  }

</style>
