package com.example.zhengxiaohu.devicemonitor;

/**
 * Created by XiaoHu Zheng on 2018/1/18.
 * Email: 1050087728@qq.com
 */

public class MessageEvent {

    public final String apiServer;

    public final String updateInterval;

    public MessageEvent(String apiServer,String updateInterval){

        this.apiServer=apiServer;
        this.updateInterval=updateInterval;
    }
}
