package com.example.zhengxiaohu.devicemonitor.device_list;

import com.example.zhengxiaohu.devicemonitor.api.data.ControllerInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.DescriptionInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.OeeInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.StatusInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.TimersInfo;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class ListItem {

    public String uniqueId;
    public int index;
    public boolean enabled;

    public DescriptionInfo descriptionInfo;
    public ControllerInfo controllerInfo;
    public OeeInfo oeeInfo;
    public StatusInfo statusInfo;
    public TimersInfo timersInfo;

    public ListItem() {

        descriptionInfo = new DescriptionInfo();
        controllerInfo = new ControllerInfo();
        oeeInfo = new OeeInfo();
        statusInfo=new StatusInfo();
        timersInfo=new TimersInfo();
    }
}
