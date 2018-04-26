<template>
  <div>
    <div style="width: 750px;height: 100px;align-items: center;justify-content: center;background-color: #ff5a37;">
      <text style="font-size: 28px">refresh-loader sample</text>
    </div>
    <refresh-loader :fetch-data="fetchData" :disable-paging="false" @receiveData="receiveData" ref="listView">
      <cell>
        <div style="width: 750px;height: 100px;align-items: center;justify-content: center">
          <text>i am header</text>
        </div>
      </cell>
      <cell v-for="(item,index) in dataList">
        <div class="item">
          <text class="item-text">{{item}}</text>
        </div>
      </cell>
    </refresh-loader>
  </div>
</template>

<script>

  import {WxcLoading, WxcResult} from 'weex-ui';
  import RefreshLoader from '../components/refresh-loader';

  export default {
    components: {WxcLoading, WxcResult, RefreshLoader},
    data: () => ({
      dataList: []
    }),
    methods: {
      fetchData(pagePos, pageSize) {
        return new Promise(((resolve, reject) => {
          setTimeout(() => {
            let data = [];
            let temp = pagePos * pageSize;
            let index = temp;
            while (index < temp + pageSize) {
              data.push(index);
              index++;
            }
            if (pagePos < 2) {
              resolve({code: 10000, message: 'success', data});
            } else {
              data.pop();
              // resolve({code: 10000, message: 'success', data: []});
              reject({});
            }
            // reject({})
          }, 2000);
        }))
      },
      receiveData(dataList) {
        this.dataList = dataList;
      }
    },
    created() {
    },
    mounted() {
      this.$refs.listView.startLoad();
      setTimeout(() => {
        console.log('manual refresh')
        this.$refs.listView.manualRefresh();
      }, 6000);
    },
    computed: {}
  }
</script>
<style scoped>

  .item {
    width: 750px;
    height: 100px;
    background-color: #cccccc;
    margin: 5px;
    justify-content: center;
    align-items: center;
  }

  .item-text {
    font-size: 30px;
  }

</style>
