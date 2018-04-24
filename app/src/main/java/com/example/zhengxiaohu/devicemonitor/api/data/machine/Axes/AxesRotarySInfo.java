package com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class AxesRotarySInfo {

    public String name="Spindle";

    public String mAngle;
    public String mRotaryVelocity;
    public String mLoad;

    public static AxesRotarySInfo parse(JSONObject json){

        AxesRotarySInfo result=new AxesRotarySInfo();

        if (json!=null){

            result.mAngle =json.optString("sangle");
            result.mRotaryVelocity =json.optString("spindle_rotary_velocity");
            result.mLoad =json.optString("spindle_load");

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

    public String getRotaryVelocity() {
        return mRotaryVelocity;
    }

    public void setRotaryVelocity(String rotaryVelocity) {
        this.mRotaryVelocity = rotaryVelocity;
    }

    public String getLoad() {
        return mLoad;
    }

    public void setLoad(String load) {
        this.mLoad = load;
    }
}
