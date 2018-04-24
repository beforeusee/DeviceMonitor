package com.example.zhengxiaohu.devicemonitor.api.data.sensor.electricalEnergy;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2018/4/19.
 * Email: 1050087728@qq.com
 */

public class ElectricalEnergyInfo {

    public String Name="ElectricalEnergyInfo";

    public String electricalEnergy;
    public String power;

    public static ElectricalEnergyInfo parse(JSONObject json){

        ElectricalEnergyInfo result=new ElectricalEnergyInfo();

        if (json!=null){

            result.electricalEnergy=json.optString("electrical_energy");
            result.power=json.optString("power");

            return result;
        }

        return null;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getElectricalEnergy() {
        return electricalEnergy;
    }

    public void setElectricalEnergy(String electricalEnergy) {
        this.electricalEnergy = electricalEnergy;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
