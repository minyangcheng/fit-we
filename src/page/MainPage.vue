<template>
  <div>
    <text class="message11">main page11</text>
    <text class="button" @click="go">go to setting page</text>
    <image style="width:500px;height:500px;" src="fit://assets/img/logo.png"></image>
  </div>
</template>
<script>
  import logoImage from '../assets/img/logo.png'

  let tool = weex.requireModule('tool');
  tool.printLog(logoImage);
  export default {
    data() {
      return {
        // logoImage:logoImage,
        aa: 1,
        bb: 11
      }
    },
    created() {
    },
    methods: {
      go() {
        this.$router.push({path: '/SettingPage'})
      }
    }
  }
</script>
<style scoped>
  .message {
    color: #333333;
    font-size: 36px;
    margin: 10px 10px;
  }

  .button {
    background-color: #cccccc;
    color: #333333;
    padding: 10px 10px;
    margin: 10px 10px;
  }
</style>
