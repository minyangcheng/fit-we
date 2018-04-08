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
        items: ['printLog111161'],
      }
    },
    methods: {
      onItemClick(item, index) {
        switch (item) {
          case 'printLog':
            this.$tool.printLog('this is printLogTest');
            this.$tool.printLog({name:'minych'});
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
