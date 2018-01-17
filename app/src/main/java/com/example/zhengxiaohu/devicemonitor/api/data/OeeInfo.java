package com.example.zhengxiaohu.devicemonitor.api.data;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class OeeInfo {
    public double oee;
    public double availability;
    public double performance;
    public double quality;

    public OeeInfo() {

        oee = 0;
        availability = 0;
        performance = 0;
        quality = 0;
    }

    public static OeeInfo parse(JSONObject json) {

        OeeInfo result = new OeeInfo();

        if (json != null) {

            result.oee = json.optDouble("oee");
            result.availability = json.optDouble("availability");
            result.performance = json.optDouble("performance");
            result.quality = json.optDouble("quality");

            return result;
        }

        return null;
    }
}
