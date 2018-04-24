package com.example.zhengxiaohu.devicemonitor.api.data.machine;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class TimersInfo {

    public double total;

    public double active;
    public double idle;
    public double alert;

    public TimersInfo() {

        total = 0;

        active = 0;
        idle = 0;
        alert = 0;

    }

    public static TimersInfo parse(JSONObject json) {

        TimersInfo result = new TimersInfo();

        if (json != null) {

            result.total = json.optDouble("total");
            result.active = json.optDouble("active");
            result.idle = json.optDouble("idle");
            result.alert = json.optDouble("alert");

            return result;
        }

        return null;
    }
}
