## RefreshLoader

> 下拉刷新、上拉加载组件，可用于列表信息展示

### 规则

- 简化上拉刷新、下拉加载页面书写
- 可以定制数据为空、数据加载异常页面显示数据和控制按钮功能
- 将请求数据操作外置到外部页面，方便控制数据操作

## 使用方法

```javascript
<template>
  <div>
    <div style="width: 750px;height: 100px;align-items: center;justify-content: center;background-color: #ff5a37;">
      <text style="font-size: 28px">refresh-loader sample</text>
    </div>
    <refresh-loader :fetch-data="fetchData" :disable-paging="false" @receiveData="receiveData" :showPagingFooter="false" ref="listView">
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

  import RefreshLoader from '../components/refresh-loader';

  export default {
    components: {RefreshLoader},
    data: () => ({
      dataList: []
    }),
    methods: {
      fetchData(pagePos, pageSize) {
        return new Promise(((resolve, reject) => {
          setTimeout(() => {
            let data = [];
            let temp = pagePos * 5;
            let index = temp;
            while (index < temp + 15) {
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

```

### 可配置参数

| Prop             | Type       | Required | Default                                                    | Description                          |     |
|:---------------- | ---------- | -------- | ---------------------------------------------------------- | ------------------------------------ | --- |
| fetchData        | `Function` | `Y`      | `-`                                                        | 请求数据方法，返回Promise对象                   |     |
| disablePaging    | `Boolean`  | `N`      | `false`                                                    | 是否禁用分页                               |     |
| showPagingFooter | `Boolean`  | `N`      | `true`                                                     | 分页加载的时候是否显示                          |     |
| emptyStatusSet   | `Object`   | `N`      | `{button: '点击加载', content: '暂无数据', handleFun: null}`       | 当数据为空时，界面显示配置，`handleFun`为点击按钮时的钩子   |     |
| errorStatusSet   | `Object`   | `N`      | `{button: '点击重试', content: '网络异常,请稍后重试', handleFun: null}` | 当数据加载异常时，界面显示配置，`handleFun`为点击按钮时的钩子 |     |



### Slot

1. `<slot></slot>`：列表项卡槽



### 事件回调

1. `@receiveData="receiveData"`数据加载成功回调



### 方法

1. `this.$refs.listView.startLoad()`: 开始页面初次加载数据

2. `this.$refs.listView.manualRefresh();`主动刷新列表


