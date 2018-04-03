<template>
  <div class="content">
    <text class="message1111">setting page</text>
    <text class="button" @click="close()">close</text>
  </div>
</template>
<script>

  export default {
    data() {
      return {}
    },
    created() {
      let tool = weex.requireModule('tool');
      tool.getLocation({x: 1, y: 1}, (value) =>{
        tool.printLog(JSON.stringify(value));
        tool.printLog(this.$router.currentRoute.path)
      },(error)=>{
        tool.printLog(error);
      });
      var globalEvent = weex.requireModule('globalEvent');
      globalEvent.addEventListener("geolocation", function (e) {
        tool.printLog("get geolocation")
      });
    },
    methods: {
      close() {
      }
    }
  }

</script>
<style scoped>
  .content {
    flex: 1;
    background-color: #cccccc;
  }

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
