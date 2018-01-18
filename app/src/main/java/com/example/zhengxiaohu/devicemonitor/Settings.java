package com.example.zhengxiaohu.devicemonitor;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        apiServerEditText.setText(ApiConfiguration.getSaveHost());
        updateIntervalEditText.setText(String.valueOf(ApiConfiguration.getUpdateInterval()));
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

                ApiConfiguration.setSavedHost(apiServerEditText.getText().toString());
            }else {

                // TODO: 2018/1/17 Api Server编辑框为空的提示文字
            }

            if (!updateIntervalEditText.getText().toString().trim().isEmpty()){

                int updateInterval=Integer.valueOf(updateIntervalEditText.getText().toString().trim());
                ApiConfiguration.setUpdateInterval(updateInterval);
            }else {

                // TODO: 2018/1/17  更新周期编辑框为空的提示文字
            }

            // post message that NavigationView中的Api Server 和Update Interval
            EventBus.getDefault().post(new MessageEvent(ApiConfiguration.getSaveHost(),String.valueOf(ApiConfiguration.getUpdateInterval())));

            onBackPressed();
        }
    }
}
