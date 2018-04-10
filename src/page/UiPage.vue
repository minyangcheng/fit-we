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
        items: ['toast', 'alert', 'pickDate', 'pickMonth', 'pickTime', 'showLoadingDialog', 'closeLoadingDialog'],
      }
    },
    methods: {
      onItemClick(item, index) {
        switch (item) {
          case 'toast':
            this.$ui.toast('this is toast test');
            break;
          case 'alert':
            this.$ui.alert({
              title: '提示',
              message: '要么进化，要么去死',
              buttonLabels: ['取消', '去进化'],
              cancelable: 1,
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result));
              },
              error(err) {
                Vue.prototype.$ui.toast(JSON.stringify(err));
              }
            });
            break;
          case 'pickDate':
            this.$ui.pickDate({
              datetime: '2018-03-01',
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result));
              },
              error(err) {
                Vue.prototype.$ui.toast(JSON.stringify(err));
              }
            });
            break;
          case 'pickMonth':
            this.$ui.pickMonth({
              datetime: '2018-07',
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result));
              },
              error(err) {
                Vue.prototype.$ui.toast(JSON.stringify(err));
              }
            });
            break;
          case 'pickTime':
            this.$ui.pickTime({
              time: '18:07',
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result));
              },
              error(err) {
                Vue.prototype.$ui.toast(JSON.stringify(err));
              }
            });
            break;
          case 'showLoadingDialog':
            this.$ui.showLoadingDialog({message: 'd', cancelable: 0});
            setTimeout(() => {
              this.$ui.closeLoadingDialog();
            }, 3000);
            break;
          case 'closeLoadingDialog':
            this.$ui.closeLoadingDialog();
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
