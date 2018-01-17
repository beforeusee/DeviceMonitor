package com.example.zhengxiaohu.devicemonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Loading extends AppCompatActivity {

    public static String loadingText="loading_text";

    public static Loading instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent myIntent=getIntent();
        setLoadingText(myIntent.getStringExtra(loadingText));

        //set status bar color
        setStatusBar();

        instance=this;
    }

    public static void open(Context context,String loadingText){

        if (instance==null){
            Intent loading=new Intent(context,Loading.class);
            loading.putExtra(Loading.loadingText,loadingText);
            context.startActivity(loading);
        }else {
            instance.setLoadingText(loadingText);
        }
    }

    public static void close(){

        instance=null;
    }
    private void setLoadingText(String loadingText) {

        View txt=findViewById(R.id.LoadingText);
        if (txt!=null){
            ((TextView)txt).setText(loadingText);
        }
    }

    private void setStatusBar(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            Window window=this.getWindow();

            //clear FLAG_TRANSLUCENT_STATUS
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            //finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.accent_normal_color));
        }
    }
}
