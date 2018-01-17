package com.example.zhengxiaohu.devicemonitor.tools;

/**
 * Created by XiaoHu Zheng on 2017/12/25.
 * Email: 1050087728@qq.com
 */

public class DM_Tools {

    public static String capitalizeFirst(final String line){
        if (line!=null&&line.trim().length()>1)
            return Character.toUpperCase(line.charAt(0))+line.substring(1);
        else
            return null;
    }
}
