package com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class AxesRotaryBInfo {

    public String name="B";

    public String mAngle;
    public String mAngularVelocity;
    public String mLoad;

    public static AxesRotaryBInfo parse(JSONObject json){

        AxesRotaryBInfo result=new AxesRotaryBInfo();

        if (json!=null){

            result.mAngle =json.optString("bangle");
            result.mAngularVelocity =json.optString("bangular_velocity");
            result.mLoad =json.optString("bload");

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

    public String getAngle() {
        return mAngle;
    }

    public void setAngle(String angle) {
        this.mAngle = angle;
    }

    public String getAngularVelocity() {
        return mAngularVelocity;
    }

    public void setAngularVelocity(String angularVelocity) {
        this.mAngularVelocity = angularVelocity;
    }

    public String getLoad() {
        return mLoad;
    }

    public void setLoad(String load) {
        this.mLoad = load;
    }
}
