package com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class AxesLinearZInfo {

    public String name="Z";

    public String position;
    public String axisFeedRate;
    public String load;

    public static AxesLinearZInfo parse(JSONObject json){

        AxesLinearZInfo result=new AxesLinearZInfo();

        if (json!=null){

            result.position=json.optString("zpos");
            result.axisFeedRate=json.optString("zaxis_feedrate");
            result.load=json.optString("zload");

            return result;
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAxisFeedRate() {
        return axisFeedRate;
    }

    public void setAxisFeedRate(String axisFeedRate) {
        this.axisFeedRate = axisFeedRate;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }
}
