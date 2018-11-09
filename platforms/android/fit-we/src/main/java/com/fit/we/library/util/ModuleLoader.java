package com.fit.we.library.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitConstants;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.WXComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created by minych on 18-4-6.
 */

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
                WXSDKEngine.registerComponent(moduleName, clazz);
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
