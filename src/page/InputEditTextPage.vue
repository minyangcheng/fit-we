<template>
  <scroller style="flex: 1">
    <text>111111</text>
    <text class="btn" @click="clear">clear</text>
    <text class="btn" @click="show">show</text>
    <text class="btn" @click="setInputText">setInputText</text>
    <text class="btn" @click="getInputText">getInputText</text>
    <text class="btn" @click="setKeyType">setKeyType</text>
    <InputEditText class="input" ref="inputRef" :value="value" :decimal-len="2" placeholder="this is placeholder"
                   return-key-type="search" @input="onInputEvent" :disabled="false"/>
    <InputEditText class="input" ref="inputRef" :value="value" :decimal-len="2" placeholder="this is placeholder"
                   return-key-type="search" @input="onInputEvent" :disabled="false"/>
    <InputEditText class="input" ref="inputRef" :value="value" :decimal-len="2" placeholder="this is placeholder"
                   return-key-type="search" @input="onInputEvent" :disabled="false"/>
    <InputEditText class="input" ref="inputRef" :value="value" :decimal-len="2" placeholder="this is placeholder"
                   return-key-type="search" @input="onInputEvent" :disabled="false"/>
    <InputEditText class="input" ref="inputRef" :value="value" :decimal-len="2" placeholder="this is placeholder"
                   return-key-type="search" @input="onInputEvent" :disabled="false"/>
    <InputEditText class="input" ref="inputRef" :value="value" :decimal-len="2" placeholder="this is placeholder"
                   return-key-type="search" @input="onInputEvent" :disabled="false"/>
  </scroller>
</template>

<script>
  export default {
    data() {
      return {
        num: 2,
        value: '',
        keyType: 'decimal',
        temp: true
      }
    },
    created() {
    },
    methods: {
      clear() {
        this.$refs.inputRef.clear();
      },
      show() {
        this.$refs.inputRef.show(false);
      },
      setInputText() {
        this.num++;
        this.$refs.inputRef.setInputText(this.num);
      },
      getInputText() {
        console.log('getInputText: ' + this.value)
        this.$ui.toast('-->' + this.value)
      },
      setKeyType() {
        this.keyType = 'decimal';
      },
      onInputEvent(e) {
        this.value = e.value;
        this.$ui.toast('-->' + this.value)
        console.log('onInputEvent: ' + e.value)
      }
    }
  }
</script>

<style scoped>
  .btn {
    height: 90px;
    line-height: 90px;
    text-align: center;
    margin: 20px;
    color: #ffffff;
    background-color: #ff5a37;
    font-size: 35px;
  }

  .input {
    font-size: 45px;
    width: 600px;
    height: 120px;
    margin: 10px;
    border-color: #ff5a37;
    border-style: solid;
    border-width: 2px;
    padding-left: 30px;
    padding-right: 30px;
  }
</style>
