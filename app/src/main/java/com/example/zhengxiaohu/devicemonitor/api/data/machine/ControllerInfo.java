package com.example.zhengxiaohu.devicemonitor.api.data.machine;

import org.json.JSONObject;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class ControllerInfo {

    public String availability;
    public String emergencyStop;
    public String controllerMode;
    public String executionMode;
    public String systemStatus;
    public String systemMessage;
    public String programName;
    public String pathFeedRate;

    public static ControllerInfo parse(JSONObject json){

        ControllerInfo result=new ControllerInfo();
        if (json!=null){
            result.availability=json.optString("available");
            result.emergencyStop=json.optString("estop");
            result.controllerMode=json.optString("controller_mode");
            result.executionMode=json.optString("execution");
            result.systemStatus=json.optString("system_status");
            result.systemMessage=json.optString("system_message");
            result.programName=json.optString("program");
            result.pathFeedRate=json.optString("path_feedrate");

            return result;
        }
        return null;
    }

}
