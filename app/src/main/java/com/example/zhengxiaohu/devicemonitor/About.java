package com.example.zhengxiaohu.devicemonitor;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MyApplication.setCurrentActivity(this);

        //set Status Bar Color
        setStatusBar();

        try{
            PackageInfo packageInfo=getPackageManager().getPackageInfo(getPackageName(),0);
            String version=packageInfo.versionName;

            ((TextView)findViewById(R.id.Version)).setText(version);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setStatusBar(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar_color));
        }
    }
}
