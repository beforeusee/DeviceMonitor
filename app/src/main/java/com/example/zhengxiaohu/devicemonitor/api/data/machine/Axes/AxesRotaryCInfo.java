package com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class AxesRotaryCInfo {

    public String mName ="C";

    public String mAngle;
    public String mAngularVelocity;
    public String mLoad;

    public static AxesRotaryCInfo parse(JSONObject json){

        AxesRotaryCInfo result=new AxesRotaryCInfo();
        if (json!=null){

            result.mAngle =json.optString("cangle");
            result.mAngularVelocity =json.optString("cangular_velocity");
            result.mLoad =json.optString("cload");

            return result;
        }
        return null;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
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
