package com.example.zhengxiaohu.devicemonitor.api.data.sensor.chatter;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class ChatterVibrationInfo {

    public String name="ChatterVibrationInfo";

    public Boolean isChatterVibration;

    public static ChatterVibrationInfo parse(JSONObject json){

        ChatterVibrationInfo result=new ChatterVibrationInfo();

        if (json!=null){

            String chatterVibration=json.optString("chatter_vibration");

            if (!chatterVibration.isEmpty()){
                if (chatterVibration.equals("TRUE")){
                    result.isChatterVibration=true;
                }
                if (chatterVibration.equals("FALSE")){
                    result.isChatterVibration=false;
                }

                return result;
            }else {

                result.isChatterVibration=false;
            }

        }

        return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChatterVibration() {
        return isChatterVibration;
    }

    public void setChatterVibration(Boolean chatterVibration) {
        isChatterVibration = chatterVibration;
    }
}
