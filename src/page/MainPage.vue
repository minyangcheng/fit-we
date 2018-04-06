<template>
  <div>
    <text class="message11">main page11</text>
    <text class="button" @click="go">go to setting page</text>
    <image style="width:500px;height:500px;" src="fit://assets/img/logo.png"></image>
  </div>
</template>
<script>
  // let tool = weex.requireModule('tool');
  // tool.printLog(logoImage);
  import config from '../../fit-library/index'

  export default {
    data() {
      return {}
    },
    created() {
      Object.keys(this.$tool).forEach(value => {
        console.log(value)
      })
      this.$tool.printLog({message:'fuck'});
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
