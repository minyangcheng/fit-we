# 如何使用模块

## 功能实现思路

### weex端
1. 从api定义文件中获取到定义的模块名那（page.name）和方法名（page.apis[0].namespace），并且模块名和方法名都是和原生端定义的扩展模块文件一一对应
2. 采用js的Object.defineProperty方法将以模块名命名的对象挂载在Vue原型对象上去
3. 在每个模块内部定义方法，并以方法名称为api定义文件中的方法名
4. 每个模块中的方法最终会调用到统一函数

```javascript
module.exports = function callInner(options, resolve, reject) {
  var data = Object.assign({}, options);
  data.success = undefined;
  data.error = undefined;
  var success = options.success;
  if (!success) {
    success = function () {
    };
  }
  var error = options.error;
  if (!error) {
    error = function () {
    };
  }
  var moduleName = this.api.moduleName;
  var namespace = this.api.namespace;
  var weexModule = weex.requireModule(moduleName);
  if (weexModule && weexModule[namespace]) {
    weexModule[namespace](data, function (value) {
      success && success(value);
      resolve && resolve(value);
    }, function (err) {
      error && error(err);
      reject && reject(err);
    });
  } else {
    var log = `weex can not find ${moduleName}.${namespace}`;
    console.error(log);
  }
};
```

### 原生端
1. 在app启动的时候，扫描所有以`_fit.json`结尾的文件
2. 循环取出每个模块，如果该模块继承自`WXComponent`表示则该模块属于UI模块，直接注入weex中;如果继承自`WXModule`，则每个用`@JSMethod`注解的方法参数必须要为`JSONObject params, JSCallback successCallback, JSCallback errorCallback`，如果模块中出现一个不合法的方法，则会忽视该模块，否则注册。
3. 原生端和weex端传输数据采用JSONObject，并且一定还要定义一个成功回调函数和一个失败回调函数

```
public class ModuleLoader {

    public static void loadModuleFromAsset(Context context) {
        try {
            String[] fileArr = context.getAssets().list("");
            String fileName = null;
            for (int i = 0; i < fileArr.length; i++) {
                fileName = fileArr[i];
                if (fileName.endsWith(FitConstants.MODULE_FILE_SUFFIX)) {
                    String moduleJsonStr = AssetsUtil.getFromAssets(context, fileName);
                    if (!TextUtils.isEmpty(moduleJsonStr)) {
                        JSONObject jsonObject = JSON.parseObject(moduleJsonStr);
                        setupModule(context, jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupModule(Context context, JSONObject jsonObject) {
        if (context == null || jsonObject == null || jsonObject.size() == 0) {
            return;
        }
        String moduleName = null;
        String className = null;
        Class clazz = null;
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            moduleName = entry.getKey();
            className = entry.getValue().toString();
            parseModule(moduleName, className);
        }
    }

    private static void parseModule(String moduleName, String className) {
        try {
            Class clazz = Class.forName(className);
            if (WXComponent.class.isAssignableFrom(clazz)) {
                FitLog.d(FitConstants.LOG_TAG, "registerComponent-->%s", className);
                WXSDKEngine.registerComponent(moduleName, clazz, false);
            } else if (WXModule.class.isAssignableFrom(clazz)) {
                checkModule(clazz);
                FitLog.d(FitConstants.LOG_TAG, "registerModule-->%s", className);
                WXSDKEngine.registerModule(moduleName, clazz);
            }
        } catch (Exception e) {
            FitLog.e(FitConstants.LOG_TAG, e);
        }
    }

    private static void checkModule(Class clazz) {
        Method[] methodArr = clazz.getDeclaredMethods();
        Method method = null;
        for (int i = 0; i < methodArr.length; i++) {
            method = methodArr[i];
            if (method.getAnnotation(JSMethod.class) != null) {
                checkMethod(clazz, method);
            }
        }
    }

    private static void checkMethod(Class clazz, Method method) {
        if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && method.getReturnType().toString().equals("void")) {
            Class[] paramClassArr = method.getParameterTypes();
            if (paramClassArr != null && paramClassArr.length == 3) {
                if (paramClassArr[0] == JSONObject.class && paramClassArr[1] == JSCallback.class && paramClassArr[2] == JSCallback.class) {
                    return;
                }
            }
        }
        throw new RuntimeException("module " + clazz.getCanonicalName() + " has JSMethod define error , the method is " + method.toString());
    }

}
```

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

## 调用外部扩展的模块

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
```
