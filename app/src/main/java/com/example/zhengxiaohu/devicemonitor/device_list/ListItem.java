package com.example.zhengxiaohu.devicemonitor.device_list;

import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesLinearXInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesLinearYInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesLinearZInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotaryAInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotaryBInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotaryCInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.Axes.AxesRotarySInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.ControllerInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.DescriptionInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.StatusInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.sensor.chatter.ChatterVibrationInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.sensor.electricalEnergy.ElectricalEnergyInfo;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class ListItem {

    public String uniqueId;
    public int index;
    public boolean enabled;

    //机床
    public DescriptionInfo mDescriptionInfo;
    public ControllerInfo mControllerInfo;
    public StatusInfo mStatusInfo;
    public AxesLinearXInfo mXInfo;
    public AxesLinearYInfo mYInfo;
    public AxesLinearZInfo mZInfo;
    public AxesRotarySInfo mSInfo;
    public AxesRotaryAInfo mAInfo;
    public AxesRotaryBInfo mBInfo;
    public AxesRotaryCInfo mCInfo;

    //传感器监测
    public ChatterVibrationInfo mChatterVibrationInfo;
    public ElectricalEnergyInfo mElectricalEnergyInfo;


    public ListItem() {

        mDescriptionInfo = new DescriptionInfo();
        mControllerInfo = new ControllerInfo();
        mStatusInfo =new StatusInfo();
        mXInfo=new AxesLinearXInfo();
        mYInfo=new AxesLinearYInfo();
        mZInfo=new AxesLinearZInfo();
        mSInfo=new AxesRotarySInfo();
        mAInfo=new AxesRotaryAInfo();
        mBInfo=new AxesRotaryBInfo();
        mCInfo=new AxesRotaryCInfo();

        mChatterVibrationInfo=new ChatterVibrationInfo();
        mElectricalEnergyInfo =new ElectricalEnergyInfo();
    }
}
