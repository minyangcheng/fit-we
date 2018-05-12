<template>
  <div>
    <div class="btn" @click="changeClass">
    </div>
    <div :style="styleObject">
    </div>
    <input ref="input"  type="text" placeholder="please input" return-key-type="search" class="input" @change="onchange" @return = "onreturn" @input="oninput" @focus="onfocus" @blur="onblur" />
  </div>
</template>

<script>
  export default {
    data() {
      return {
        type: 1,
      }
    },
    methods: {
      changeClass() {
        this.type = 2;
        // this.$refs.input.setSelectionRange(1,3);
        // this.$refs.input.input({value:'content'})
        this.$refs.input.setTextFormatter({
          formatRule:/\d+/,
          formatReplace:'q'
        })
      },
      onchange(e){
        console.log('onchange:'+e.value)
      },
      oninput(e){
        console.log('oninput:'+e.value)
      },
      onreturn(e){
        console.log('onreturn:'+e.value)
      },
      onfocus(e){
        console.log('onfocus:'+e.value)
      },
      onblur(e){
        console.log('onblur:'+e.value)
      }
    },
    computed: {
      classObject() {
        let classArr = [];
        if (this.type == 1) {
          classArr.push('item-1');
        }
        if (this.type == 2) {
          classArr.push('item-2');
        }
        return classArr
      },
      styleObject() {
        return {
          width: '750px',
          height: '90px',
          backgroundColor: this.type==1?'#000000':'#ee33e2'
        }
      }
    }
  }
</script>

<style scoped>
  .btn {
    width: 750px;
    height: 90px;
    background-color: #ff5a37;
  }

  .item-1 {
    width: 750px;
    height: 90px;
    background-color: #000000;
  }

  .item-2 {
    width: 750px;
    height: 90px;
    background-color: #ee33e2;
  }

  .item-3 {
    width: 750px;
    height: 90px;
    background-color: #cccccc;
  }

  .input{
    font-size: 45px;
    width: 600px;
    margin: 10px;
    border-color: #ff5a37;
    border-style: solid;
    border-width: 2px;
    padding: 20px;
  }
</style>
