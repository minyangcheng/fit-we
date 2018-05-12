<template>
  <div style="flex: 1">
    <wxc-loading :show="loadTypeFlag" :need-mask="true"
                 :mask-style="{backgroundColor:'rgba(0, 0, 0, 0)'}"></wxc-loading>
    <wxc-loading :show="manualRefreshing&&refreshing" :need-mask="true"
                 :mask-style="{backgroundColor:'rgba(0, 0, 0, 0)'}"></wxc-loading>
    <list v-if="contentTypeFlag" @loadmore="onLoad" loadmoreoffset="100">
      <refresh class="refresh" @refresh="onRefresh" :display="refreshing ? 'show' : 'hide'">
        <loading-indicator class="refresh-loading-indicator"></loading-indicator>
      </refresh>
      <slot></slot>
      <cell v-if="showPagingFooter&&loading">
        <div class="loading">
          <text class="loading-text">加载中...</text>
        </div>
      </cell>
    </list>
    <wxc-result type="noGoods"
                :show="emptyTypeFlag"
                :custom-set="emptySet"
                padding-top="232"
                @wxcResultButtonClicked="emptyRetry"></wxc-result>
    <wxc-result type="errorPage"
                :show="errorTypeFlag"
                :custom-set="errorSet"
                padding-top="232"
                @wxcResultButtonClicked="errorRetry"></wxc-result>
  </div>
</template>

<script>

  import {WxcLoading, WxcResult} from 'weex-ui';

  var modal = weex.requireModule('modal');

  const LOAD_TYPE = 1;
  const CONTENT_TYPE = 2;
  const EMPTY_TYPE = 3;
  const ERROR_TYPE = 4;
  const HINT_MESSAGE = '网络异常,请稍后重试';

  export default {
    components: {WxcLoading, WxcResult},
    props: {
      fetchData: {
        type: Function,
        required: true,
      },
      disablePaging: {
        type: Boolean,
        required: false,
      },
      showPagingFooter:{
        type: Boolean,
        default: true
      },
      emptyStatusSet: {
        type: Object,
        default: {button: '点击加载', content: '暂无数据', handleFun: null}
      },
      errorStatusSet: {
        type: Object,
        default: {button: '点击重试', content: '网络异常,请稍后重试', handleFun: null}
      },
    },
    data: () => ({
      dataList: [],
      refreshing: false,
      manualRefreshing:false,
      loading: false,
      loadFinally: false,
      pageType: CONTENT_TYPE,
      pagePos: 0,
      pageSize: 15,
    }),
    methods: {
      onRefresh() {
        this.refreshing = true;
        this.loadFinally = false;
        this.loadData(0, true);
      },
      onLoad() {
        if (this.disablePaging || this.loadFinally || this.loading) {
          return;
        }
        this.loading = true;
        this.loadData(this.pagePos, false);
      },
      emptyRetry() {
        if (this.emptyStatusSet.handle && this.emptyStatusSet instanceof Function) {
          this.emptyStatusSet.handle();
        } else {
          this.startLoad();
        }
      },
      errorRetry() {
        if (this.errorStatusSet.handle && this.errorStatusSet instanceof Function) {
          this.errorStatusSet.handle();
        } else {
          this.startLoad();
        }
      },
      showPageType(type = CONTENT_TYPE) {
        this.pageType = type;
      },
      startLoad() {
        this.showPageType(LOAD_TYPE);
        this.onRefresh();
      },
      loadData(pos, refreshFlag) {
        this.fetchData(pos, this.pageSize).then(resp => {
          if (refreshFlag) {
            this.setRefreshDataSuccess(resp.data);
          } else {
            this.setLoadMoreSuccess(resp.data);
          }
          this.manualRefreshing=false;
        }).catch(error => {
          if (refreshFlag) {
            this.setRefreshDataFail(error);
          } else {
            this.setLoadMoreFail(error);
          }
          this.manualRefreshing=false;
        });
      },
      judgeLoadFinally(receiveList) {
        if (!this.disablePaging) {
          if (!receiveList || receiveList.length < this.pageSize) {
            this.loadFinally = true;
          } else {
            this.loadFinally = false;
          }
        }
      },
      setRefreshDataSuccess(receiveList) {
        if (!receiveList || receiveList.length == 0) {
          this.showPageType(EMPTY_TYPE);
        } else {
          this.showPageType(CONTENT_TYPE);
        }
        this.refreshing = false;
        this.judgeLoadFinally(receiveList);

        this.dataList = receiveList || [];
        this.$emit('receiveData', this.dataList);
        if (!this.disablePaging) {
          this.pagePos = 1;
        }
      },
      setRefreshDataFail(error) {
        if (!this.dataList || this.dataList.length == 0) {
          this.showPageType(ERROR_TYPE);
        } else {
          modal.toast({
            message: error.message || HINT_MESSAGE,
            duration: 0.3
          })
        }
        this.refreshing = false;
      },
      setLoadMoreSuccess(receiveList) {
        if (receiveList && receiveList.length > 0) {
          this.dataList = this.dataList.concat(receiveList);
          this.pagePos++;
          this.$emit('receiveData', this.dataList);
        }
        this.judgeLoadFinally(receiveList);
        this.loading = false;
      },
      setLoadMoreFail(error) {
        modal.toast({
          message: error.message || HINT_MESSAGE,
          duration: 0.3
        })
        this.loading = false;
      },
      manualRefresh(){
        this.manualRefreshing=true;
        this.onRefresh();
      }
    },
    created() {
    },
    computed: {
      loadTypeFlag() {
        return this.pageType === LOAD_TYPE;
      },
      contentTypeFlag() {
        return this.pageType === CONTENT_TYPE;
      },
      emptyTypeFlag() {
        return this.pageType === EMPTY_TYPE;
      },
      errorTypeFlag() {
        return this.pageType === ERROR_TYPE;
      },
      emptySet() {
        return this.emptyStatusSet ? {noGoods: this.emptyStatusSet} : {};
      },
      errorSet() {
        return this.errorStatusSet ? {errorPage: this.errorStatusSet} : {};
      }
    }
  }
</script>
<style scoped>

  .refresh {
    align-items: center;
    padding: 15px;
  }

  .refresh-loading-indicator {
    width: 60px;
    height: 60px;
    color: #f0573b;
  }

  .loading {
    align-items: center;
    padding: 20px;
  }

  .loading-text {
    color: #666666;
  }

</style>
