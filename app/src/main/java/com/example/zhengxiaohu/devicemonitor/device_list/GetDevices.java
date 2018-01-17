package com.example.zhengxiaohu.devicemonitor.device_list;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zhengxiaohu.devicemonitor.DeviceList;
import com.example.zhengxiaohu.devicemonitor.MainActivity;
import com.example.zhengxiaohu.devicemonitor.MyApplication;
import com.example.zhengxiaohu.devicemonitor.api.ApiConfiguration;
import com.example.zhengxiaohu.devicemonitor.api.http.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class GetDevices extends AsyncTask<String,Void,ListItem[]> {

    private DeviceList deviceList;
    private Context context;

    public GetDevices(DeviceList deviceList,Context context){
        this.deviceList=deviceList;
        this.context=context;
    }

    public GetDevices(Context context){
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        MainActivity.error=false;
    }

    @Override
    protected ListItem[] doInBackground(String... params) {

        ListItem[] result=null;

        result=getListItems();
        return new ListItem[0];
    }

    @Override
    protected void onPostExecute(ListItem[] listItems) {

        MyApplication.Devices=null;
        MyApplication.ListItems=null;

//        d5e321c3-2ab0-4f7b-b871-960041a36b6d/status
        MyApplication.ListItems=listItems;

        if (deviceList!=null){
            deviceList.addDevices();
            deviceList.hideLoading();
        }
        super.onPostExecute(listItems);
    }

    public static ListItem[] getListItems(){

        ListItem[] listItems=null;

        //从Analytics Server处获取MTConnect设备的ListItem的信息的命令字，需在服务端实现相应的REST模块
        String QUERY_INDEX="data_mobile_api/get";

        String url= Uri.withAppendedPath(ApiConfiguration.apiHost,QUERY_INDEX).toString();
        String response= Requests.get(url);

//        String response=Requests.post(url,null);

        return processResponse(response);
    }

    private static ListItem[] processResponse(String response){

        if (response!=null&&response.length()>0){

            try{

                //Get Parent Array from response string
                JSONArray jsonArray=new JSONArray(response);
                return processResponseArray(jsonArray);

            }catch (JSONException ex){

                Log.d("Exception",ex.getMessage());

                MainActivity.error=true;
            }
        }
        return null;
    }
    private static ListItem[] processResponseArray(JSONArray a){

        if (a!=null&&a.length()>0){

            //Create ArrayList to return
            ArrayList<ListItem> result=new ArrayList<>();

            try{
                for (int i=0;i<a.length();i++){

                    JSONObject object=a.optJSONObject(i);

                    ListItem item=new ListItem();
                    String device_id=object.getString("device_id");
                    String address=object.getString("address");
                }
            }catch (JSONException e){

                Log.d("Exception",e.getMessage());

                MainActivity.error=true;
            }
        }else {

            MainActivity.error=true;
        }
        return null;
    }
}
