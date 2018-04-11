# 如何使用模块

## 调用模块

有两种调用方式：
* 回调方式
```javascript
this.$ui.alert({
              title: '提示',
              message: '去进化，皮卡丘',
              buttonLabels: ['取消', '去进化'],
              cancelable: 1,
              success(result) {
                Vue.prototype.$ui.toast(JSON.stringify(result));
              },
              error(err) {
                Vue.prototype.$ui.toast(JSON.stringify(err));
              }
            });
```
* promise回调
```javascript
this.$ui.alert({
              title: '提示',
              message: '去进化，皮卡丘',
              buttonLabels: ['取消', '去进化'],
              cancelable: 1
            }).then(value => {
              this.$ui.toast(JSON.stringify(result));
            }).catch(err => {
              this.$ui.toast(JSON.stringify(err));
            });
```

## 前端定义api文件

api对象上的模块名和定义方法名、需要传递的参数、兼容调用处理。之后会根据这些信息在Vue的原型对象上，通过js Object.defineProperty
生成一个以模块名命名的对象，该对象中的方法就是此处定义的方法。
当你用`this.$ui.toast('hello')`的时候，就会自动调用原生写好的$ui模块上的toast方法。

```javascript
module.exports = {
  name: '$ui',
  apis: [
    {
      namespace: 'toast',
      defaultParams: {
        message: '',
      },
      runCode(...rest) {
        const args = compatibleStringParamsToObject.call(this, rest, 'message');
        callInner.apply(this, args);
      },
    },
    {
      namespace: 'alert',
      defaultParams: {
        title: '',
        message: '',
        buttonLabels: ['确定'],
        cancelable: 1,
      },
    }
  ]
}
```

## 原生端定义module

* 定义原生模块
原生模块中的每个用`@JSMethod`注解的方法参数必须要为`JSONObject params, JSCallback successCallback, JSCallback errorCallback`，如果模块中出现一个不合法的方法，则会忽视该模块。

```
public class UiModule extends WXModule {

    /**
     * 消息提示
     * message： 需要提示的消息内容
     * duration：显示时长,long或short
     */
    @JSMethod(uiThread = true)
    public void toast(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            String message = params.getString("message");
            String duration = params.getString("duration");
            if (!TextUtils.isEmpty(message)) {
                if ("long".equalsIgnoreCase(duration)) {
                    Toast.makeText(container.getActivity(), message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(container.getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
            successCallback.invoke(null);
        }
    }

    /**
     * 弹出确认对话框
     * title：标题
     * message：消息
     * cancelable：是否可取消
     * buttonLabels：按钮数组，最多设置2个按钮
     * 返回：
     * which：按钮id
     */
    @JSMethod(uiThread = true)
    public void alert(JSONObject params, final JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            String title = params.getString("title");
            String message = params.getString("message");
            JSONArray jsonArray = params.getJSONArray("buttonLabels");
            String btnName_1 = null;
            String btnName_2 = null;
            if (jsonArray != null && jsonArray.size() > 0) {
                if (jsonArray.size() == 1) {
                    btnName_1 = jsonArray.getString(0);
                } else {
                    btnName_1 = jsonArray.getString(0);
                    btnName_2 = jsonArray.getString(1);
                }
            } else {
                btnName_1 = "确定";
            }
            boolean cancelable = !"0".equals(params.getString("cancelable"));
            DialogUtil.showAlertDialog(container.getActivity(), title, message, btnName_1, btnName_2, cancelable, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JSONObject data = new JSONObject();
                    data.put("which", 0);
                    successCallback.invoke(data);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JSONObject data = new JSONObject();
                    data.put("which", 1);
                    successCallback.invoke(data);
                }
            });
        }
    }
 }
```

* 在以`_fit.json`文件定义该模块(android环境下该文件可放在assets文件夹中)，以便fit-we的模块加载器可以扫描到该模块
```json
{
    "$navigator": "com.fit.we.library.extend.module.NavigatorModule",
    "$page": "com.fit.we.library.extend.module.PageModule",
    "$tool": "com.fit.we.library.extend.module.ToolModule",
    "$ui": "com.fit.we.library.extend.module.UiModule"
}
```

### 调用外部扩展的模块

调用$pay模块上的payMoney

```javascript
this.$zeus.callApi({
              module: '$pay',
              name: 'payMoney',
              money: '100',
            }).then((value) => {
              this.$ui.toast(JSON.stringify(value))
            }).catch(err => {
              this.$ui.toast(JSON.stringify(err))
            });
            break;
```



