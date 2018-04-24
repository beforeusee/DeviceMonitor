package com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class AxesLinearXInfo {

    public String name;

    public String position;
    public String axisFeedRate;
    public String load;

    public static AxesLinearXInfo parse(JSONObject json){

        AxesLinearXInfo result=new AxesLinearXInfo();
        if (json!=null){
            result.position=json.optString("xpos");
            result.axisFeedRate=json.optString("xaxis_feedrate");
            result.load=json.optString("xload");

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
