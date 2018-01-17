package com.example.zhengxiaohu.devicemonitor;

import android.app.Activity;
import android.app.Application;

import com.example.zhengxiaohu.devicemonitor.device_list.ListItem;
import com.example.zhengxiaohu.devicemonitor.devices.Device;

/**
 * Created by XiaoHu Zheng on 2017/12/23.
 * Email: 1050087728@qq.com
 */

public class MyApplication extends Application {

    public static Device[] Devices;

    public static ListItem[] ListItems;

    private static Activity currentActivity=null;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        MyApplication.currentActivity = currentActivity;
    }
}
