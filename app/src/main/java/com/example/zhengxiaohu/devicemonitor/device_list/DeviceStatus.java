package com.example.zhengxiaohu.devicemonitor.device_list;

import android.net.Uri;
import android.util.Log;

import com.example.zhengxiaohu.devicemonitor.api.ApiConfiguration;
import com.example.zhengxiaohu.devicemonitor.api.data.OeeInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.StatusInfo;
import com.example.zhengxiaohu.devicemonitor.api.http.Requests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class DeviceStatus {
    public String uniqueId;

    public StatusInfo statusInfo;
    public OeeInfo oeeInfo;

    public DeviceStatus() {

        uniqueId = null;

        statusInfo = new StatusInfo();
        oeeInfo = new OeeInfo();

    }

    public static DeviceStatus[] get() {

        DateTime now = DateTime.now();
        DateTime from = new DateTime(now.year().get(), now.monthOfYear().get(), now.dayOfMonth().get(), 0, 0, 0);
        DateTime to = from.plusDays(1);

        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        String fromStr = fmt.print(from);
        String toStr = fmt.print(to);

                /*
                String urlSuffix = "data/get/?" +
                        "token=" + URLEncoder.encode(userConfig.sessionToken, "UTF-8") +
                        "&sender_id=" + URLEncoder.encode(UserManagement.getSenderId(), "UTF-8") +
                        "&from=" + fromStr + "&to=" + toStr +
                        "&command=0101"; // Get Status and Oee tables
                        */
        String url = Uri.withAppendedPath(ApiConfiguration.apiHost, "urlSuffix").toString();

        String response = Requests.get(url);
        if (response != null && response.length() > 0) {

            ArrayList<DeviceStatus> devicesStatuses = new ArrayList<>();

            try {

                JSONArray a = new JSONArray(response);

                for (int i = 0; i < a.length(); i++) {

                    JSONObject obj = a.getJSONObject(i);

                    DeviceStatus deviceStatus = new DeviceStatus();

                    deviceStatus.uniqueId = obj.getString("unique_id");

                    deviceStatus.statusInfo = StatusInfo.parse(obj.getJSONObject("status"));

                    deviceStatus.oeeInfo = OeeInfo.parse(obj.getJSONObject("oee"));

                    devicesStatuses.add(deviceStatus);
                }
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

            DeviceStatus[] deviceStatusArray = new DeviceStatus[devicesStatuses.size()];
            return devicesStatuses.toArray(deviceStatusArray);
        }


        return null;
    }
}
