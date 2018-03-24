<template>
  <div>
    <text class="message">setting page</text>
    <text class="button" @click="close()">close</text>
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
      close() {

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
