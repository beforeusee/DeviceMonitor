package com.example.zhengxiaohu.devicemonitor.device_list;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.zhengxiaohu.devicemonitor.DeviceList;
import com.example.zhengxiaohu.devicemonitor.MyApplication;
import com.example.zhengxiaohu.devicemonitor.api.http.Requests;
import com.example.zhengxiaohu.devicemonitor.devices.Device;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class GetLogos extends AsyncTask<String,Void,String> {

    private DeviceList context;

    public GetLogos(DeviceList context){

        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        ListItem[] listItems= MyApplication.ListItems;
        if (listItems!=null){

        }
        return null;
    }


    public static boolean setLogoImage(Device device){

        if (device.LogoUrl!=null){

            String url="http://www.feenux.com/trakhound/users/files"+device.LogoUrl;

            Bitmap logo= Requests.getImage(url);

            if (logo!=null){
                device.Logo=logo;
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onPostExecute(String s) {

        if (context!=null){
            context.addDevices();
        }
    }
}
