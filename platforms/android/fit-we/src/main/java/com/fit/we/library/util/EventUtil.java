package com.fit.we.library.util;


import de.greenrobot.event.EventBus;

/**
 * Created by minyangcheng on 2017/3/31.
 */

public class EventUtil {

    public static void register(Object obj){
        if(obj==null) return;
        EventBus.getDefault().register(obj);
    }

    public static void unregister(Object obj){
        if(obj==null) return;
        EventBus.getDefault().unregister(obj);
    }

    public static void post(Object obj){
        if(obj==null) return;
        EventBus.getDefault().post(obj);
    }

}
