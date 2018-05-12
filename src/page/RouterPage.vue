<template>
  <div>
    <div v-for="(item,index) in items" :key="index" @click="onItemClick(item,index)">
      <text class="item">{{item}}</text>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        items: ['open','openLocal', 'close', 'reload'],
      }
    },
    created() {
    },
    methods: {
      onItemClick(item, index) {
        switch (item) {
          case 'open':
            this.$router.open({
              pagePath: 'fit://page/InfoPage',
              data: {name: 'minyangcheng'},
            });
            break;
          case 'openLocal':
            this.$router.openLocal({
              className: 'com.fit.we.sample.LocalActivity',
              data: {name: 'minych', age: 12},
            });
            break;
          case 'close':
            this.$router.close();
            break;
          case 'reload':
            this.$router.reload();
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
