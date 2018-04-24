package com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class AxesLinearYInfo {

    public String name="Y";

    public String position;
    public String axisFeedRate;
    public String load;

    public static AxesLinearYInfo parse(JSONObject json){

        AxesLinearYInfo result=new AxesLinearYInfo();

        if (json!=null){

            result.position=json.optString("ypos");
            result.axisFeedRate=json.optString("yaxis_feedrate");
            result.load=json.optString("yload");

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
