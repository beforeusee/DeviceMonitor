package com.example.zhengxiaohu.devicemonitor.api.data.machine;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class DescriptionInfo {
    // Description
    public String description;
    public String manufacturer;
    public String model;
    public String deviceId;
    public String name;
    public String uuid;
    public String address;
    public String port;
    public String serial;

    public static DescriptionInfo parse(JSONObject json) {

        DescriptionInfo result = null;

        if (json != null) {

            result = new DescriptionInfo();

            result.description=json.optString("description");
            result.manufacturer=json.optString("manufacturer");
            result.model=json.optString("model");

            result.deviceId = json.optString("device_id");
            result.name=json.optString("name");
            result.uuid=json.optString("uuid");
            result.address=json.optString("address");
            result.port=json.optString("port");
        }

        return result;
    }
}
