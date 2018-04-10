<template>
  <scroller>
    <text style="padding:30px 30px;font-size: 30px">{{message}}</text>
    <text style="padding:30px 30px;font-size: 30px">{{weexConfig}}</text>
  </scroller>
</template>

<script>
  export default {
    data() {
      return {
        message:'注: routeInfo:跳转页面传参信息\n' +
        'nativeParams:应用公共参数信息\n' +
        'bundleUrl:页面资源路径',
        weexConfig:'',
      }
    },
    created(){
      this.weexConfig=JSON.stringify(weex.config,null,4);
    },
    methods: {

    }
  }
</script>

<style scoped>

</style>
