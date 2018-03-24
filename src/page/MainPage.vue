<template>
  <div>
    <text class="message">main page</text>
    <text class="button" @click="go">go to setting page</text>
  </div>
</template>
<script>
  export default {
    data() {
      return {}
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
