package com.example.zhengxiaohu.devicemonitor.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class ApiConfiguration {

    public static Context context;
    private String s;

    public static Uri apiHost=Uri.parse("http://192.168.43.161:8015/");
//    public static Uri apiHost=Uri.parse("https://api.trakhound.com/");


    public String host;
    public String path;

    public static String getSaveHost(){
        if (context!=null){
            SharedPreferences prefs=context.getSharedPreferences("org.trakhound.www.trakhound",Context.MODE_PRIVATE);
            if (prefs!=null){
                return prefs.getString("api_host",null);
            }
        }
        return null;
    }

    public static void setSavedHost(String host) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().putString("api_host", host).apply();
        }
    }

    public static String getSavedPath() {

        if (context != null) {

            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) {

                return prefs.getString("api_path", null);
            }
        }

        return null;
    }

    public static void setSavedPath(String path) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().putString("api_path", path).apply();
        }
    }
}
