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
        items: ['hide', 'show', 'showStatusBar', 'hideStatusBar', 'hideBackBtn','hookSysBack', 'hookBackBtn', 'setTitle', 'setRightBtn', 'setRightMenu', 'setLeftBtn'],
      }
    },
    created(){
      this.$event.post('testType', {name:'min',age:1});
    },
    methods: {
      onItemClick(item, index) {
        switch (item) {
          case 'hide':
            this.$navigator.hide();
            break;
          case 'show':
            this.$navigator.show();
            break;
          case 'showStatusBar':
            this.$navigator.showStatusBar();
            break;
          case 'hideStatusBar':
            this.$navigator.hideStatusBar();
            break;
          case 'hideBackBtn':
            this.$navigator.hideBackBtn();
            break;
          case 'hookSysBack':
            this.$navigator
              .hookSysBack(() => this.$ui.toast('you have click sys back'));
            break;
          case 'hookBackBtn':
            this.$navigator
              .hookBackBtn(() => this.$ui.toast('you have click navigation back'));
            break;
          case 'setTitle':
            this.$navigator.setTitle("Navigator");
            break;
          case 'setRightBtn':
            this.$navigator.setRightBtn({
              text: '关于',
              imageUrl: '',
              isShow: 1,
              which: 0,
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result));
              },
              error(error) {

              }
            });
            // this.$navigator.setRightBtn({
            //   text: '设置',
            //   imageUrl: 'http://10.10.12.153:8080/static/img/setting.png',
            //   isShow: 1,
            //   // 对应哪一个按钮，一般是0, 1可选择
            //   which: 1,
            //   success(result) {
            //     hybrid.ui.toast(JSON.stringify(result))
            //   },
            //   error(error) {
            //
            //   }
            // });
            break;
          case 'setRightMenu':
            this.$navigator.setRightMenu({
              text: '菜单',
              titleItems: ['帮助', '反馈'],
              iconItems: ['http://10.10.12.153:8080/static/img/setting.png', 'http://10.10.12.153:8080/static/img/setting.png'],
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result))
              },
              error(error) {
              }
            });
            break;
          case 'setLeftBtn':
            this.$navigator.setLeftBtn({
              text: '设置',
              isShow: 1,
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result));
              },
              error(error) {

              }
            });
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
