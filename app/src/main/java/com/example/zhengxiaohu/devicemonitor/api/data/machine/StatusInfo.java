package com.example.zhengxiaohu.devicemonitor.api.data.machine;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class StatusInfo {

    public Boolean connected;

    public String deviceStatus;
    public String productionStatus;
    public Integer deviceStatusTimer;
    public Integer productionStatusTimer;


    public static StatusInfo parse(JSONObject json) {

        StatusInfo result = null;

        if (json != null) {

            result = new StatusInfo();

            result.connected=json.optBoolean("connected");

            result.deviceStatus = json.optString("device_status");
            result.productionStatus = json.optString("production_status");
            result.deviceStatusTimer = json.optInt("device_status_timer");
            result.productionStatusTimer = json.optInt("production_status_timer");
        }

        return result;
    }
}
