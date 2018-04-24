package com.example.zhengxiaohu.devicemonitor.device_details;

import android.net.Uri;

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
import com.example.zhengxiaohu.devicemonitor.api.data.machine.OeeInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.StatusInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.TimersInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.sensor.chatter.ChatterVibrationInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.sensor.electricalEnergy.ElectricalEnergyInfo;
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

    //机床
    public DescriptionInfo mDescriptionInfo;
    public ControllerInfo mControllerInfo;
    public StatusInfo mStatusInfo;
    public AxesLinearXInfo mXInfo;
    public AxesLinearYInfo mYInfo;
    public AxesLinearZInfo mZInfo;
    public AxesRotarySInfo mSInfo;
    public AxesRotaryAInfo mAInfo;
    public AxesRotaryBInfo mBInfo;
    public AxesRotaryCInfo mCInfo;

    //传感器监测
    public ChatterVibrationInfo mChatterVibrationInfo;
    public ElectricalEnergyInfo mElectricalEnergyInfo;

    public DeviceStatus(){

        uniqueId=null;
        mStatusInfo =new StatusInfo();
        mControllerInfo =new ControllerInfo();
        mDescriptionInfo=new DescriptionInfo();

        mXInfo=new AxesLinearXInfo();
        mYInfo=new AxesLinearYInfo();
        mZInfo=new AxesLinearZInfo();
        mSInfo=new AxesRotarySInfo();
        mAInfo=new AxesRotaryAInfo();
        mBInfo=new AxesRotaryBInfo();
        mCInfo=new AxesRotaryCInfo();

        mChatterVibrationInfo=new ChatterVibrationInfo();
        mElectricalEnergyInfo =new ElectricalEnergyInfo();
    }

    public static DeviceStatus get(String uniqueId){

        DateTime now=DateTime.now();
        DateTime from=new DateTime(now.year().get(),now.monthOfYear().get(),now.dayOfMonth().get(),0,0,0);
        DateTime to=from.plusDays(1);

        DateTimeFormatter fmt= ISODateTimeFormat.dateTime();
        String fromStr=fmt.print(from);
        String toStr=fmt.print(to);


        String urlSuffix=uniqueId+"/monitor";

        String url= Uri.withAppendedPath(ApiConfiguration.apiHost,urlSuffix).toString();

        String response= Requests.get(url);
        if (response!=null&&response.length()>0){

            try {

                JSONObject json=new JSONObject(response);

                DeviceStatus deviceStatus=new DeviceStatus();

                deviceStatus.uniqueId=json.optString("device_id");

                deviceStatus.mStatusInfo =StatusInfo.parse(json);

                deviceStatus.mControllerInfo =ControllerInfo.parse(json);

                deviceStatus.mDescriptionInfo=DescriptionInfo.parse(json);

                deviceStatus.mXInfo=AxesLinearXInfo.parse(json);
                deviceStatus.mYInfo=AxesLinearYInfo.parse(json);
                deviceStatus.mZInfo=AxesLinearZInfo.parse(json);
                deviceStatus.mSInfo=AxesRotarySInfo.parse(json);
                deviceStatus.mAInfo=AxesRotaryAInfo.parse(json);
                deviceStatus.mBInfo=AxesRotaryBInfo.parse(json);
                deviceStatus.mCInfo=AxesRotaryCInfo.parse(json);

                deviceStatus.mChatterVibrationInfo=ChatterVibrationInfo.parse(json);
                deviceStatus.mElectricalEnergyInfo=ElectricalEnergyInfo.parse(json);

                return deviceStatus;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
