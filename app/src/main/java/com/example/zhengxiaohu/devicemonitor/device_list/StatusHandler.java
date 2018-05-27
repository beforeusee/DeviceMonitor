package com.example.zhengxiaohu.devicemonitor.device_list;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.zhengxiaohu.devicemonitor.DeviceList;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class StatusHandler implements Runnable {

    private Context context;

    public StatusHandler(Context context) {

        this.context = context;
    }


    @Override
    public void run() {

        boolean exit = false;

        while (!exit) {

            try
            {
                boolean connected = isNetworkConnected();


                // Only run if connected to Wifi or Ethernet
                if (connected) {

                    DeviceStatus[] statuses = DeviceStatus.get();
                    if (statuses != null) {

                        ((DeviceList) context).updateStatus(statuses);
                    }
                }

                ((DeviceList) context).updateConnectionStatus(connected);

                Thread.sleep(5000);
            }
            catch (InterruptedException ex) {  }
        }
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected;

        boolean isWifi;
        boolean isEthernet;

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            isConnected = true;
            isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            isEthernet = activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET;
        }
        else {
            isConnected = false;
            isWifi=false;
            isEthernet=false;
        }

        return isConnected && (isWifi || isEthernet);
    }
}
