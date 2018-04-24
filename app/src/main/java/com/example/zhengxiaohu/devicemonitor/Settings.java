package com.example.zhengxiaohu.devicemonitor;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhengxiaohu.devicemonitor.api.ApiConfiguration;

import org.greenrobot.eventbus.EventBus;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    EditText apiServerEditText;
    EditText updateIntervalEditText;
    Button saveButton;

    SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();

        initData();
        saveButton.setOnClickListener(this);

    }

    private void initData() {

        apiServerEditText.setText(ApiConfiguration.apiHost.toString());
        updateIntervalEditText.setText(String.valueOf(ApiConfiguration.apiUpdateInterval));
    }

    private void initView() {

        apiServerEditText= (EditText) findViewById(R.id.ApiServer);
        updateIntervalEditText= (EditText) findViewById(R.id.UpdateInterval);
        saveButton= (Button) findViewById(R.id.Save);

    }

    @Override
    public void onClick(View v) {

        int id=v.getId();

        if (id==R.id.Save){

            //save api server and update interval
            if (!apiServerEditText.getText().toString().trim().isEmpty()){

                ApiConfiguration.apiHost= Uri.parse(apiServerEditText.getText().toString());
                ApiConfiguration.setSavedHost(this,apiServerEditText.getText().toString());
            }else {

                // Api Server编辑框为空的提示文字
                Toast.makeText(this,"Api Server为空",Toast.LENGTH_SHORT).show();
            }

            if (!updateIntervalEditText.getText().toString().trim().isEmpty()){

                int updateInterval=Integer.valueOf(updateIntervalEditText.getText().toString().trim());
                ApiConfiguration.apiUpdateInterval=updateInterval;
                ApiConfiguration.setUpdateInterval(this,updateInterval);
            }else {

                // 更新周期编辑框为空的提示文字
                Toast.makeText(this,"Update Interval为空",Toast.LENGTH_SHORT).show();
            }

            // post event message 更新NavigationView中的Api Server 和Update Interval
            EventBus.getDefault().post(new MessageEvent(ApiConfiguration.getSaveHost(this),String.valueOf(ApiConfiguration.getUpdateInterval(this))));

            onBackPressed();
        }
    }
}
