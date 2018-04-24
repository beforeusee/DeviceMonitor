package com.example.zhengxiaohu.devicemonitor.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class ApiConfiguration {

    Context mContext;

    public static Uri apiHost=Uri.parse("http://192.168.43.165:8015/");
    public static int apiUpdateInterval=500;
//    public static Uri apiHost=Uri.parse("https://api.trakhound.com/");

    public String host;
    public String path;

    public static String getSaveHost(Context context){

        if (context!=null){
            SharedPreferences prefs=context.getSharedPreferences("ApiServerAndUpdateInterval",Context.MODE_PRIVATE);
            if (prefs!=null){

                return prefs.getString("api_host",null);
            }
        }
        return null;
    }

    public static void setSavedHost(Context context,String host) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("ApiServerAndUpdateInterval", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().putString("api_host", host).apply();
        }
    }

    public static int getUpdateInterval(Context context){

        if (context!=null){

            SharedPreferences preferences=context.getSharedPreferences("ApiServerAndUpdateInterval",Context.MODE_PRIVATE);
            if (preferences!=null){
                return preferences.getInt("update_interval",-1);
            }
        }
        return -1;
    }

    public static void setUpdateInterval(Context context,int updateInterval){

        if (context!=null){

            SharedPreferences preferences=context.getSharedPreferences("ApiServerAndUpdateInterval",Context.MODE_PRIVATE);
            if (preferences!=null){
                preferences.edit().putInt("update_interval",updateInterval).apply();
            }
        }
    }


    public static String getSavedPath(Context context) {

        if (context != null) {

            SharedPreferences prefs = context.getSharedPreferences("ApiServerAndUpdateInterval", Context.MODE_PRIVATE);
            if (prefs != null) {

                return prefs.getString("api_path", null);
            }
        }

        return null;
    }

    public static void setSavedPath(Context context,String path) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("ApiServerAndUpdateInterval", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().putString("api_path", path).apply();
        }
    }
}
