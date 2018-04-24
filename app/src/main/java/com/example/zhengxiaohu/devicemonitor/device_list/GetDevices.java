package com.example.zhengxiaohu.devicemonitor.device_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zhengxiaohu.devicemonitor.DeviceList;
import com.example.zhengxiaohu.devicemonitor.Loading;
import com.example.zhengxiaohu.devicemonitor.MainActivity;
import com.example.zhengxiaohu.devicemonitor.MyApplication;
import com.example.zhengxiaohu.devicemonitor.api.ApiConfiguration;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesLinearXInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesLinearYInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesLinearZInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotaryAInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotaryBInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotaryCInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotarySInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.ControllerInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.DescriptionInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.StatusInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.sensor.chatter.ChatterVibrationInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.sensor.electricalEnergy.ElectricalEnergyInfo;
import com.example.zhengxiaohu.devicemonitor.api.http.Requests;
import com.example.zhengxiaohu.devicemonitor.devices.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class GetDevices extends AsyncTask<String,Void,ListItem[]> {

    private DeviceList deviceList;
    private Context context;

    public GetDevices(DeviceList deviceList){
        this.deviceList=deviceList;
        this.context=deviceList;
    }

    public GetDevices(Context context){
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        MainActivity.error=false;
    }

    @Override
    protected ListItem[] doInBackground(String... params) {

        ListItem[] result=null;

        result=getListItems();

        return result;
    }

    @Override
    protected void onPostExecute(ListItem[] listItems) {

        MyApplication.Devices=null;
        MyApplication.ListItems=null;

        MyApplication.ListItems=listItems;

        if (deviceList!=null){

            deviceList.addDevices();

            deviceList.hideLoading();

        }else {
            if (listItems==null){

                if (context!=null){
                    context.startActivity(new Intent(context,MainActivity.class));
                }
            }else {

                Intent deviceListIntent=new Intent(context, DeviceList.class);
                deviceListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                //Open the Device List Page
                context.startActivity(deviceListIntent);
            }
        }
    }

    public static ListItem[] getListItems(){

        ListItem[] listItems=null;

        //从AnalyticsServer处获取MTConnect设备的ListItem的信息的命令字，需在服务端实现相应的REST模块
        String QUERY_INDEX="monitors";

        String url= Uri.withAppendedPath(ApiConfiguration.apiHost,QUERY_INDEX).toString();
        String response= Requests.get(url);

//        String response=Requests.post(url,null);

        return processResponse(response);
    }

    private static ListItem[] processResponse(String response){

        if (response!=null&&response.length()>0){

            try{

                //Get Parent Array from response string
                JSONArray jsonArray=new JSONArray(response);

                return processResponseArray(jsonArray);

            }catch (JSONException ex){

                Log.d("Exception",ex.getMessage());

                MainActivity.error=true;
                return null;
            }
        }else {
            return null;
        }
    }
    private static ListItem[] processResponseArray(JSONArray a){

        if (a!=null&&a.length()>0){

            //Create ArrayList to return
            ArrayList<ListItem> result=new ArrayList<>();

            for (int i=0;i<a.length();i++){

                JSONObject json=a.optJSONObject(i);

                ListItem item=new ListItem();

                item.index=i;
                item.mDescriptionInfo= DescriptionInfo.parse(json);
                item.mControllerInfo= ControllerInfo.parse(json);
                item.mStatusInfo= StatusInfo.parse(json);
                item.mXInfo= AxesLinearXInfo.parse(json);
                item.mYInfo= AxesLinearYInfo.parse(json);
                item.mZInfo= AxesLinearZInfo.parse(json);
                item.mSInfo= AxesRotarySInfo.parse(json);
                item.mAInfo= AxesRotaryAInfo.parse(json);
                item.mBInfo= AxesRotaryBInfo.parse(json);
                item.mCInfo= AxesRotaryCInfo.parse(json);

                item.mChatterVibrationInfo= ChatterVibrationInfo.parse(json);
                item.mElectricalEnergyInfo = ElectricalEnergyInfo.parse(json);

                item.uniqueId=item.mDescriptionInfo.deviceId;

                result.add(item);
            }

            ListItem[] resultArray=new ListItem[result.size()];

            for (int i=0;i<resultArray.length;i++){

                resultArray[i]=result.get(i);
            }

            return resultArray;

        }else {

            MainActivity.error=true;
            return null;
        }
    }
}
