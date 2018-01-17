package com.example.zhengxiaohu.devicemonitor.device_details;

import android.net.Uri;

import com.example.zhengxiaohu.devicemonitor.api.ApiConfiguration;
import com.example.zhengxiaohu.devicemonitor.api.data.ControllerInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.OeeInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.StatusInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.TimersInfo;
import com.example.zhengxiaohu.devicemonitor.api.http.Requests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class DeviceStatus {

    public String uniqueId;

    public StatusInfo statusInfo;
    public ControllerInfo controllerInfo;
    public OeeInfo oeeInfo;
    public TimersInfo timersInfo;

    public DeviceStatus(){

        uniqueId=null;
        statusInfo=new StatusInfo();
        controllerInfo=new ControllerInfo();
        oeeInfo=new OeeInfo();
        timersInfo=new TimersInfo();
    }

    public static DeviceStatus get(String uniqueId){

        DateTime now=DateTime.now();
        DateTime from=new DateTime(now.year().get(),now.monthOfYear().get(),now.dayOfMonth().get(),0,0,0);
        DateTime to=from.plusDays(1);

        DateTimeFormatter fmt= ISODateTimeFormat.dateTime();
        String fromStr=fmt.print(from);
        String toStr=fmt.print(to);

        //// TODO: 2017/12/27 此处去除了token和sender_id
        String urlSuffix="data/get/?"+
                "devices=[{\"unique_id\":\"" + uniqueId + "\"}]"+
                "&from=" + fromStr + "&to=" + toStr +
                "&command=" + "01111"; // Get Status, Controller, Oee, and Timers tables;

        String url= Uri.withAppendedPath(ApiConfiguration.apiHost,urlSuffix).toString();

        String response= Requests.get(url);
        if (response!=null&&response.length()>0){

            try {
                JSONArray jsonArray=new JSONArray(response);

                if (jsonArray.length()>0){

                    JSONObject object=jsonArray.getJSONObject(0);

                    DeviceStatus deviceStatus=new DeviceStatus();

                    deviceStatus.uniqueId=object.getString("unique_id");

                    deviceStatus.statusInfo=StatusInfo.parse(object.getJSONObject("status"));

                    deviceStatus.controllerInfo=ControllerInfo.parse(object.getJSONObject("controller"));

                    deviceStatus.oeeInfo=OeeInfo.parse(object.getJSONObject("oee"));

                    deviceStatus.timersInfo=TimersInfo.parse(object.getJSONObject("timers"));

                    return deviceStatus;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
