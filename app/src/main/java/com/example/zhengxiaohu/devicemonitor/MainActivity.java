package com.example.zhengxiaohu.devicemonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static boolean error;
    public static String showError="show_error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.setCurrentActivity(this);

        //set Status Bar Color
        setStatusBar();

        if (savedInstanceState==null){

            Bundle extras=getIntent().getExtras();
            if (extras!=null){
                error=extras.getBoolean(showError);
            }
        }

        if (error){
//            showError();
        }

        Context context=getApplicationContext();
        Intent deviceListIntent=new Intent(context,DeviceList.class);
        deviceListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
        Intent.FLAG_ACTIVITY_CLEAR_TASK|
        Intent.FLAG_ACTIVITY_NEW_TASK);

        //Open the Device List Page
        context.startActivity(deviceListIntent);
    }

    /*
    private void showError() {

        TextView errorTextView = (TextView)findViewById(R.id.ErrorLabel);
        if (errorTextView != null) errorTextView.setVisibility(View.VISIBLE);
    }
    */

    private void setStatusBar(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar_color));
        }
    }
}
