package com.example.zhengxiaohu.devicemonitor.device_list;

import android.net.Uri;
import android.util.Log;

import com.example.zhengxiaohu.devicemonitor.api.ApiConfiguration;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.OeeInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.StatusInfo;
import com.example.zhengxiaohu.devicemonitor.api.http.Requests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class DeviceStatus {

    public String uniqueId;

    public StatusInfo mStatusInfo;

    public DeviceStatus() {

        uniqueId = null;

        mStatusInfo = new StatusInfo();

    }

    public static DeviceStatus[] get() {

        String urlConnectionsSuffix = "connections";
        String urlConnections = Uri.withAppendedPath(ApiConfiguration.apiHost, urlConnectionsSuffix).toString();

        String urlStatusSuffix="status";

        String response = Requests.get(urlConnections);
        if (response != null && response.length() > 0) {

            ArrayList<DeviceStatus> devicesStatuses = new ArrayList<>();

            try {

                JSONArray a = new JSONArray(response);

                for (int i = 0; i < a.length(); i++) {

                    JSONObject json = a.getJSONObject(i);

                    //获取机床状态数据
                    String deviceId=json.optString("device_id");


                    String statusInfoUrl=Uri.withAppendedPath(ApiConfiguration.apiHost,deviceId+"/"+urlStatusSuffix).toString();
                    String statusResponse=Requests.get(statusInfoUrl);

                    if (statusResponse!=null&&statusResponse.length()>0){

                        JSONObject jsonStatus=new JSONObject(statusResponse);

                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.uniqueId=jsonStatus.optString("device_id");
                        deviceStatus.mStatusInfo=StatusInfo.parse(jsonStatus);

                        devicesStatuses.add(deviceStatus);
                    }

                }
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

            DeviceStatus[] deviceStatusArray = new DeviceStatus[devicesStatuses.size()];
            return devicesStatuses.toArray(deviceStatusArray);
        }


        return null;
    }
}
