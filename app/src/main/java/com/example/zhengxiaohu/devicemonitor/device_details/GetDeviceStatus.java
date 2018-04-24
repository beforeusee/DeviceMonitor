package com.example.zhengxiaohu.devicemonitor.device_details;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.zhengxiaohu.devicemonitor.DeviceDetails;
import com.example.zhengxiaohu.devicemonitor.MyApplication;

/**
 * Created by XiaoHu Zheng on 2017/12/25.
 * Email: 1050087728@qq.com
 */

public class GetDeviceStatus extends AsyncTask<String,Void,DeviceStatus> {

    private DeviceDetails deviceDetails;
    private Context context;
    private String uniqueId;
    private int position;

    public GetDeviceStatus(DeviceDetails deviceDetails,String uniqueId){

        this.deviceDetails=deviceDetails;
        this.uniqueId=uniqueId;
    }

    public GetDeviceStatus(Context context,int position){
        this.context=context;
        this.position=position;

        if (MyApplication.ListItems!=null){
            if (position<MyApplication.ListItems.length){

                String uniqueId=MyApplication.ListItems[position].uniqueId;
                if (uniqueId!=null){
                    this.uniqueId=uniqueId;
                }
            }
        }
    }

    protected void onPreExecute(){
        if (deviceDetails!=null){
            deviceDetails.showLoading();
        }
    }

    @Override
    protected DeviceStatus doInBackground(String... params) {

        if (uniqueId!=null){

            DeviceStatus status=DeviceStatus.get(uniqueId);

            return status;
        }
        return null;
    }

    @Override
    protected void onPostExecute(DeviceStatus deviceStatus) {

        if (deviceDetails!=null){

            deviceDetails.loadData();

            deviceDetails.hideLoading();
        }else {

            Intent deviceDetailsIntent=new Intent(context,DeviceDetails.class);
            deviceDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //传递device在MyApplication.Devices中的数组的索引位置
            deviceDetailsIntent.putExtra(DeviceDetails.DEVICE_INDEX,position);

            //打开Device List 界面
            context.startActivity(deviceDetailsIntent);
        }

    }
}
