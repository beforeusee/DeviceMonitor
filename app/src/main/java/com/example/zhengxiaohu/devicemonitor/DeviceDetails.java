package com.example.zhengxiaohu.devicemonitor;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhengxiaohu.devicemonitor.api.ApiConfiguration;
import com.example.zhengxiaohu.devicemonitor.device_details.DeviceStatus;
import com.example.zhengxiaohu.devicemonitor.device_details.GetDeviceStatus;
import com.example.zhengxiaohu.devicemonitor.device_details.StatusHandler;
import com.example.zhengxiaohu.devicemonitor.device_list.ListItem;
import com.example.zhengxiaohu.devicemonitor.tools.MPAndroidLineChartManager;
import com.example.zhengxiaohu.devicemonitor.view.DialChartSLoadView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;
import java.util.List;


public class DeviceDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static String DEVICE_INDEX="device_index";

    public static DeviceStatus deviceStatus;

    //侧滑的导航栏
    TextView apiServerTextView;
    TextView updateIntervalTextView;

    //x
    TextView xPosView;
    TextView xAxisFeedRateView;
    DialChartSLoadView xLoadView;

    //y
    TextView yPosView;
    TextView yAxisFeedRateView;
    DialChartSLoadView yLoadView;

    //z
    TextView zPosView;
    TextView zAxisFeedRateView;
    DialChartSLoadView zLoadView;

    //S主轴
    TextView spindleVelocityView;
    DialChartSLoadView sLoadView;

    //A
    DialChartSLoadView aLoadView;
    TextView aAngularVelocity;
    TextView aAngleTextView;

    //B
    TextView bAngleTextView;
    TextView bAngularVelocity;
    DialChartSLoadView bLoadView;

    //C
    TextView cAngleTextView;
    TextView cAngularVelocity;
    DialChartSLoadView cLoadView;

    //能耗
    TextView mElectricalEnergy;

    //功率图形
    LineChart mLineChart;
    private List<Float> mPowerList=new ArrayList<>();

    private static final int X_MAX_POINT=60;
    private int mCount=0;
    private int mXAxisMin=0;

    private Toolbar toolbar;
    private Thread statusThread;
    private final Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        MyApplication.setCurrentActivity(this);

        initView();

        hideLoading();

        Intent intent=this.getIntent();

        int deviceIndex=intent.getIntExtra(DEVICE_INDEX,0);

        ListItem[] listItems=MyApplication.ListItems;
        if (listItems!=null){
            ListItem listItem=listItems[deviceIndex];
            loadDevice(listItem);
        }

        //set status bar color
        setStatusBar();

        //setup Toolbar/ActionBar
        setToolbar();

        //setup Navigation drawer
        setNavigationDrawer();

        sLoadView.invalidate();
        xLoadView.invalidate();
        yLoadView.invalidate();
        zLoadView.invalidate();
        aLoadView.invalidate();
        bLoadView.invalidate();
        cLoadView.invalidate();

    }

    private void initView() {

        //X
        xPosView= (TextView) findViewById(R.id.xPos);
        xAxisFeedRateView= (TextView) findViewById(R.id.xAxisFeedRate);
        xLoadView= (DialChartSLoadView) findViewById(R.id.x_load_view);

        //Y
        yPosView= (TextView) findViewById(R.id.yPos);
        yAxisFeedRateView= (TextView) findViewById(R.id.yAxisFeedRate);
        yLoadView= (DialChartSLoadView) findViewById(R.id.y_load_view);

        //Z
        zPosView= (TextView) findViewById(R.id.zPos);
        zAxisFeedRateView= (TextView) findViewById(R.id.zAxisFeedRate);
        zLoadView= (DialChartSLoadView) findViewById(R.id.z_load_view);

        //S
        spindleVelocityView= (TextView) findViewById(R.id.spindleVelocity);
        sLoadView= (DialChartSLoadView) findViewById(R.id.spindle_load_view);

        //A
        aLoadView= (DialChartSLoadView) findViewById(R.id.a_load_view);
        aAngularVelocity = (TextView) findViewById(R.id.aAngularVelocity);
        aAngleTextView = (TextView) findViewById(R.id.aAngle);

        //B
        bAngleTextView = (TextView) findViewById(R.id.bAngle);
        bAngularVelocity = (TextView) findViewById(R.id.bAngularVelocity);
        bLoadView = (DialChartSLoadView) findViewById(R.id.b_load_view);

        //C
        cAngleTextView = (TextView) findViewById(R.id.cAngle);
        cAngularVelocity = (TextView) findViewById(R.id.cAngularVelocity);
        cLoadView = (DialChartSLoadView) findViewById(R.id.c_load_view);

        //能耗
        mElectricalEnergy= (TextView) findViewById(R.id.ElectricalEnergy);
        //功率
        mLineChart= (LineChart) findViewById(R.id.lc_power);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (statusThread!=null){
            statusThread.interrupt();
        }
    }

    public void loadDevice(ListItem item) {

        if (deviceStatus == null) deviceStatus = new DeviceStatus();

        deviceStatus.uniqueId = item.uniqueId;
        deviceStatus.mStatusInfo = item.mStatusInfo;
        deviceStatus.mControllerInfo = item.mControllerInfo;
        deviceStatus.mDescriptionInfo=item.mDescriptionInfo;

        deviceStatus.mXInfo=item.mXInfo;
        deviceStatus.mYInfo=item.mYInfo;
        deviceStatus.mZInfo=item.mZInfo;
        deviceStatus.mSInfo=item.mSInfo;
        deviceStatus.mAInfo=item.mAInfo;
        deviceStatus.mBInfo=item.mBInfo;
        deviceStatus.mCInfo=item.mCInfo;

        deviceStatus.mChatterVibrationInfo=item.mChatterVibrationInfo;
        deviceStatus.mElectricalEnergyInfo=item.mElectricalEnergyInfo;


        //loadImages(d);

        loadDescription(item);

        loadData();

        if (statusThread == null) startStatusThread(item);
    }

    private void startStatusThread(ListItem listItem) {

        // Start Status Updates on separate thread
        StatusHandler statusHandler = new StatusHandler(this, handler, listItem);
        statusThread = new Thread(statusHandler);
        statusThread.start();
    }

    private void loadDescription(ListItem item) {

        if (item != null && item.mDescriptionInfo != null) {

            TextView txt;

            txt = (TextView) findViewById(R.id.Address);
            if (txt != null) txt.setText(item.mDescriptionInfo.address);

            txt = (TextView) findViewById(R.id.Port);
            if (txt != null) txt.setText(item.mDescriptionInfo.port);

            txt = (TextView) findViewById(R.id.Name);
            if (txt != null) txt.setText(item.mDescriptionInfo.name);

            txt = (TextView) findViewById(R.id.Uuid);
            if (txt != null) txt.setText(item.mDescriptionInfo.uuid);

            txt = (TextView) findViewById(R.id.DeviceId);
            if (txt != null) txt.setText(item.mDescriptionInfo.deviceId);
        }
    }

    public void loadData() {

        if (deviceStatus != null) {

//            updateStatus(deviceStatus);

            updateControllerStatus(deviceStatus);

            updateAxesLinearXInfo(deviceStatus);
            updateAxesLinearYInfo(deviceStatus);
            updateAxesLinearZInfo(deviceStatus);

            updateAxesRotarySInfo(deviceStatus);

            updateAxesRotaryAInfo(deviceStatus);
            updateAxesRotaryBInfo(deviceStatus);
            updateAxesRotaryCInfo(deviceStatus);

            updateChatterVibrationStatus(deviceStatus);

            updateElectricalEnergyInfo(deviceStatus);

            updatePowerInfo(deviceStatus);

        }
    }

    private void updatePowerInfo(DeviceStatus deviceStatus) {

        List<Float> x=new ArrayList<>();
        List<Float> y=new ArrayList<>();
        LineData lineData;

        if (deviceStatus!=null){

            if (!deviceStatus.mElectricalEnergyInfo.power.isEmpty()){

                mPowerList.add(Float.valueOf(deviceStatus.mElectricalEnergyInfo.power));
                mCount++;
                if (mCount<X_MAX_POINT){

                    for (int i=0;i<mPowerList.size();i++ ){
                        x.add(i,1.0f*i);
                        y.add(i,mPowerList.get(i));
                    }
                    lineData=MPAndroidLineChartManager.initSingleLineChart(this,mLineChart,x,y,"功率");

                }else {
                    //X的坐标从索引mPowerList.size()-X_MAX_POINT开始算起
                    for (int i=0;i<X_MAX_POINT;i++){

                        x.add(i,1.0f*(mPowerList.size()-X_MAX_POINT+i));
                        y.add(i,mPowerList.get(mPowerList.size()-X_MAX_POINT+i));
                    }
                    lineData=MPAndroidLineChartManager.initSingleLineChart(this,mLineChart,x,y,"功率");
                }
                MPAndroidLineChartManager.initChartStyle(this,mLineChart,lineData);
            }
        }

    }

    private void updateElectricalEnergyInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mElectricalEnergyInfo!=null){

            if (!deviceStatus.mElectricalEnergyInfo.electricalEnergy.isEmpty()){

                if (mElectricalEnergy!=null){

                    if (!deviceStatus.mElectricalEnergyInfo.electricalEnergy.isEmpty()){

                        mElectricalEnergy.setText(String.format("%.2f",Float.valueOf(deviceStatus.mElectricalEnergyInfo.electricalEnergy)/3600000));
                    }else {
                        mElectricalEnergy.setText("");
                    }
                }
            }

        }else {
            clearElectricalEnergyInfo();
        }
    }

    private void clearElectricalEnergyInfo() {

    }

    /**
     * 更新颤振状态
     * 主轴加工过程是否发生颤振
     * @param deviceStatus 设备状态
     */
    private void updateChatterVibrationStatus(DeviceStatus deviceStatus) {

        if (deviceStatus.mChatterVibrationInfo!=null){

            //根据值设置颜色
            int color=getResources().getColor(R.color.foreground_normal_color);

            ImageView vibrationImageView= (ImageView) findViewById(R.id.SpindleVibrationImage);
            TextView vibrationTextView= (TextView) findViewById(R.id.SpindleVibrationText);

            if (vibrationImageView!=null){

                //发生颤振
                if (deviceStatus.mChatterVibrationInfo.isChatterVibration){

                    vibrationImageView.setImageResource(R.drawable.chatter_03);
                    vibrationImageView.setColorFilter(ContextCompat.getColor(this,R.color.statusRed));
                    vibrationTextView.setText("CHATTER");
                    vibrationTextView.setTextColor(ContextCompat.getColor(this,R.color.statusRed));
                }else {
                    vibrationImageView.setImageResource(R.drawable.nochatter_03);
                    vibrationImageView.setColorFilter(ContextCompat.getColor(this,R.color.statusGreen));
                    vibrationTextView.setText("NORMAL");
                    vibrationTextView.setTextColor(ContextCompat.getColor(this,R.color.statusGreen));
                }
            }
        }else {

            ImageView vibrationImageView= (ImageView) findViewById(R.id.SpindleVibrationImage);
            TextView vibrationTextView= (TextView) findViewById(R.id.SpindleVibrationText);

            vibrationImageView.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
            vibrationTextView.setText("");
        }
    }

    /**
     * 更新C轴信息
     * @param deviceStatus 状态
     */
    private void updateAxesRotaryCInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mCInfo!=null){

            //角度
//            float angleDouble=Float.valueOf(deviceStatus.mCInfo.mAngle);
//            String angle=String.format("%.3f",angleDouble);

            if (cAngleTextView !=null){
                if (!(deviceStatus.mCInfo.mAngle.isEmpty()||deviceStatus.mCInfo.mAngle.equals("UNAVAILABLE"))){
                    cAngleTextView.setText(String.format("%.3f",Float.valueOf(deviceStatus.mCInfo.mAngle)));
                }else {

                    cAngleTextView.setText("");
                }
            }

            //旋转速度
            if (cAngularVelocity!=null){

                if (!(deviceStatus.mCInfo.mAngularVelocity.isEmpty()||deviceStatus.mCInfo.mAngularVelocity.equals("UNAVAILABLE"))){

                    cAngularVelocity.setText(String.format("%.0f",Float.valueOf(deviceStatus.mCInfo.mAngularVelocity)*60));
                }else {

                    cAngularVelocity.setText("");
                }
            }


            //负载
            if (cLoadView !=null){

                if (!(deviceStatus.mCInfo.mLoad.equals("")||deviceStatus.mCInfo.mLoad.equals("UNAVAILABLE"))){

                    cLoadView.setCurrentStatus(Float.valueOf(deviceStatus.mCInfo.mLoad)/100);
                    cLoadView.invalidate();
                }else {
                    cLoadView.setCurrentStatus(0.0f);
                    cLoadView.invalidate();

                }
            }

        }else {


        }
    }

    /**
     * 更新B轴信息
     * @param deviceStatus 状态
     */
    private void updateAxesRotaryBInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mBInfo!=null){

            //角度
//            float angleDouble=Float.valueOf(deviceStatus.mBInfo.mAngle);
//            String angle=String.format("%.3f",angleDouble);

            if (bAngleTextView !=null){
                if (!(deviceStatus.mBInfo.mAngle.isEmpty()||deviceStatus.mBInfo.mAngle.equals("UNAVAILABLE"))){
                    bAngleTextView.setText(String.format("%.3f",Float.valueOf(deviceStatus.mBInfo.mAngle)));
                }else {
                    bAngleTextView.setText("");
                }

            }

            //旋转速度
            if (bAngularVelocity!=null){

                if (!(deviceStatus.mBInfo.mAngularVelocity.isEmpty()||deviceStatus.mBInfo.mAngularVelocity.equals("UNAVAILABLE"))){

                    bAngularVelocity.setText(String.format("%.0f",Float.valueOf(deviceStatus.mBInfo.mAngularVelocity)*60));
                }else {

                    bAngularVelocity.setText("");
                }
            }

            //负载
            if (bLoadView !=null){

                if (!(deviceStatus.mBInfo.mLoad.equals("")||deviceStatus.mBInfo.mLoad.equals("UNAVAILABLE"))){
                    bLoadView.setCurrentStatus(Float.valueOf(deviceStatus.mBInfo.mLoad)/100);
                    bLoadView.invalidate();
                }else {
                    bLoadView.setCurrentStatus(0.0f);
                    bLoadView.invalidate();
                }
            }

        }
    }

    /**
     * 更新A轴信息
     * @param deviceStatus 状态
     */
    private void updateAxesRotaryAInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mAInfo!=null){

            //角度
//            float angleDouble=Float.valueOf(deviceStatus.mAInfo.mAngle);
//            String angle=String.format("%.3f",angleDouble);
            if (aAngleTextView !=null){

                if (!(deviceStatus.mAInfo.mAngle.isEmpty()||deviceStatus.mAInfo.mAngle.equals("UNAVAILABLE"))){

                    aAngleTextView.setText(String.format("%.3f",Float.valueOf(deviceStatus.mAInfo.mAngle)));
                }else {
                    aAngleTextView.setText("");
                }
            }

            //旋转速度
            if (aAngularVelocity !=null){

                if (!(deviceStatus.mAInfo.mAngularVelocity.isEmpty()||deviceStatus.mAInfo.mAngularVelocity.equals("UNAVAILABLE"))){

                    aAngularVelocity.setText(String.format("%.0f",Float.valueOf(deviceStatus.mAInfo.mAngularVelocity)*60));
                }else {
                    aAngularVelocity.setText("");
                }
            }


            //负载
            if (aLoadView!=null){

                if (!(deviceStatus.mAInfo.mLoad.equals("")||deviceStatus.mAInfo.mLoad.equals("UNAVAILABLE"))){
                    aLoadView.setCurrentStatus(Float.valueOf(deviceStatus.mAInfo.mLoad)/100);
                    aLoadView.invalidate();
                }else {
                    aLoadView.setCurrentStatus(0.0f);
                    aLoadView.invalidate();
                }
            }

        }
    }

    /**
     * 更新主轴信息，转速和负载
     * @param deviceStatus 状态
     */
    private void updateAxesRotarySInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mSInfo!=null){

            //主轴转速
            if (spindleVelocityView!=null){

                if (!(deviceStatus.mSInfo.mRotaryVelocity.isEmpty()||deviceStatus.mSInfo.mRotaryVelocity.equals("UNAVAILABLE"))){

                    spindleVelocityView.setText(String.format("%.0f",Float.valueOf(deviceStatus.mSInfo.mRotaryVelocity)));
                }else {
                    spindleVelocityView.setText("");
                }
            }

            //主轴负载
            if (sLoadView!=null){
                if (!(deviceStatus.mSInfo.mLoad.equals("")||deviceStatus.mSInfo.mLoad.equals("UNAVAILABLE"))){
                    sLoadView.setCurrentStatus(Float.valueOf(deviceStatus.mSInfo.mLoad)/100);
                    sLoadView.invalidate();
                }else {
                    sLoadView.setCurrentStatus(0.0f);
                    sLoadView.invalidate();

                }
            }
        }
    }

    /**
     * 更新Z轴信息
     * @param deviceStatus 设备状态
     */
    private void updateAxesLinearZInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mZInfo!=null){

            //Z轴位置
            if (zPosView!=null){

                if (!(deviceStatus.mZInfo.position.isEmpty()||deviceStatus.mZInfo.position.equals("UNAVAILABLE"))){

                    zPosView.setText(String.format("%.3f",Float.valueOf(deviceStatus.mZInfo.position)));
                }else {
                    zPosView.setText("");
                }
            }

            //z轴速度
            if (zAxisFeedRateView!=null){

                if (!(deviceStatus.mZInfo.axisFeedRate.isEmpty()||deviceStatus.mZInfo.axisFeedRate.equals("UNAVAILABLE"))){

                    zAxisFeedRateView.setText(String.format("%.0f",Float.valueOf(deviceStatus.mZInfo.axisFeedRate)*60));
                }else {
                    zAxisFeedRateView.setText("");
                }
            }

            //z轴负载
            if (zLoadView!=null){

                if (!(deviceStatus.mZInfo.load.equals("")||deviceStatus.mZInfo.load.equals("UNAVAILABLE"))){
                    zLoadView.setCurrentStatus(Float.valueOf(deviceStatus.mZInfo.load)/100);
                    zLoadView.invalidate();
                }else {
                    zLoadView.setCurrentStatus(0.0f);
                    zLoadView.invalidate();

                }

            }
        }
    }

    /**
     * 更新Y轴信息
     * @param deviceStatus 设备状态
     */
    private void updateAxesLinearYInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mYInfo!=null){

            //Y轴位置
            if (yPosView!=null){

                if (!(deviceStatus.mYInfo.position.isEmpty()||deviceStatus.mYInfo.position.equals("UNAVAILABLE"))){

                    yPosView.setText(String.format("%.3f",Float.valueOf(deviceStatus.mYInfo.position)));
                }else {
                    yPosView.setText("");
                }
            }

            //Y轴速度
            if (yAxisFeedRateView!=null){

                if (!(deviceStatus.mYInfo.axisFeedRate.isEmpty()||deviceStatus.mYInfo.axisFeedRate.equals("UNAVAILABLE"))){

                    yAxisFeedRateView.setText(String.format("%.0f",Float.valueOf(deviceStatus.mYInfo.axisFeedRate)*60));
                }else {
                    yAxisFeedRateView.setText("");
                }
            }

            //y轴负载
            if (yLoadView!=null){

                if (!(deviceStatus.mYInfo.load.equals("")||deviceStatus.mYInfo.load.equals("UNAVAILABLE"))){
                    yLoadView.setCurrentStatus(Float.valueOf(deviceStatus.mYInfo.load)/100);
                    yLoadView.invalidate();
                }else {
                    yLoadView.setCurrentStatus(0.0f);
                    yLoadView.invalidate();

                }
            }
        }
    }

    /**
     * 更新X轴信息
     * @param deviceStatus 设备状态
     */
    private void updateAxesLinearXInfo(DeviceStatus deviceStatus) {

        if (deviceStatus.mXInfo!=null){

            //X轴位置

            if (xPosView!=null){

                if (!(deviceStatus.mXInfo.position.isEmpty()||deviceStatus.mXInfo.position.equals("UNAVAILABLE"))){

                    xPosView.setText(String.format("%.3f",Float.valueOf(deviceStatus.mXInfo.position)));
                }else {
                    xPosView.setText("");
                }
            }

            //X轴速度

            if (xAxisFeedRateView!=null){

                if (!(deviceStatus.mXInfo.axisFeedRate.isEmpty()||deviceStatus.mXInfo.axisFeedRate.equals("UNAVAILABLE"))){

                    xAxisFeedRateView.setText(String.format("%.0f",Float.valueOf(deviceStatus.mXInfo.axisFeedRate)*60));
                }else {
                    xAxisFeedRateView.setText("");
                }
            }

            //X轴负载

            if (xLoadView!=null){

                if (!(deviceStatus.mXInfo.load.equals("")||deviceStatus.mXInfo.load.equals("UNAVAILABLE"))){
                    xLoadView.setCurrentStatus(Float.valueOf(deviceStatus.mXInfo.load)/100);
                    xLoadView.invalidate();
                }else {

                    xLoadView.setCurrentStatus(0.0f);
                    xLoadView.invalidate();

                }
            }
        }
    }

    /**
     * 更新控制器状态
     * @param status 状态
     */
    private void updateControllerStatus(DeviceStatus status) {

        if (status != null && status.mControllerInfo != null) {

            updateControllerStatus_Availability(status);
            updateControllerStatus_EmergencyStop(status);
            updateControllerStatus_ControllerMode(status);
            updateControllerStatus_ExecutionMode(status);
            updateControllerStatus_SystemStatus(status);
            updateControllerStatus_CurrentProgram(status);
            updateControllerStatus_PathFeedRate(status);
        }
    }

    /**
     * 更新控制器可用性
     * @param status 状态
     */
    private void updateControllerStatus_Availability(DeviceStatus status) {

        boolean avail = status.mControllerInfo.availability.equals("true");

        // Set Power On Background
        View bkgrd = findViewById(R.id.PowerOnBackground);
        if (bkgrd != null) {

            if (avail) bkgrd.setBackgroundColor(ContextCompat.getColor(this,R.color.statusGreen));
            else bkgrd.setBackgroundColor(Color.TRANSPARENT);
        }

        // Set Power Off Background
        bkgrd = findViewById(R.id.PowerOffBackground);
        if (bkgrd != null) {

            if (avail) bkgrd.setBackgroundColor(Color.TRANSPARENT);
            else bkgrd.setBackgroundColor(ContextCompat.getColor(this,R.color.statusRed));
        }


        // Set Power On Image
        ImageView img = (ImageView)findViewById(R.id.PowerOnImage);
        if (img != null) {

            if (avail) img.setColorFilter(Color.WHITE);
            else img.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
        }

        // Set Power Off Image
        img = (ImageView)findViewById(R.id.PowerOffImage);
        if (img != null) {

            if (avail) img.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
            else img.setColorFilter(Color.WHITE);
        }


        // Set Text
        TextView txt = (TextView)findViewById(R.id.AvailabilityText);
        if (txt != null) {

            if (avail) {

                txt.setText("AVAILABLE");
                txt.setTextColor(ContextCompat.getColor(this,R.color.statusGreen));
            }
            else {
                txt.setText("UNAVAILABLE");
                txt.setTextColor(ContextCompat.getColor(this,R.color.statusRed));
            }
        }
    }

    /**
     * 更新急停按钮
     * @param status 状态
     */
    private void updateControllerStatus_EmergencyStop(DeviceStatus status) {

        // Set color based on value
        int color = ContextCompat.getColor(this,R.color.foreground_normal_color);

        if (status.mControllerInfo.emergencyStop.equals("ARMED"))
            color = ContextCompat.getColor(this,R.color.statusGreen);

        else if (status.mControllerInfo.emergencyStop.equals("TRIGGERED"))
            color = ContextCompat.getColor(this,R.color.statusRed);


        // Set Image
        ImageView img = (ImageView)findViewById(R.id.EmergencyStopImage);
        if (img != null) img.setColorFilter(color);

        // Set Text
        TextView txt = (TextView)findViewById(R.id.EmergencyStopText);
        if (txt != null) {
            txt.setText(status.mControllerInfo.emergencyStop);
            txt.setTextColor(color);
        }
    }

    /**
     * 更新控制器模式
     * @param status 状态
     */
    private void updateControllerStatus_ControllerMode(DeviceStatus status) {

        String s = status.mControllerInfo.controllerMode;

        // Automatic
        ImageView img = (ImageView)findViewById(R.id.ControllerMode_Auto);
        if (img != null)
        {
            if (s.equals("AUTOMATIC")) img.setColorFilter(ContextCompat.getColor(this,R.color.statusGreen));
            else img.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
        }

        // MDI (Manual Data Input)
        img = (ImageView)findViewById(R.id.ControllerMode_MDI);
        if (img != null)
        {
            if (s.equals("MANUAL_DATA_INPUT")) img.setColorFilter(ContextCompat.getColor(this,R.color.accent_light_color));
            else img.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
        }

        // SBLK (Single Block Mode)
        img = (ImageView)findViewById(R.id.ControllerMode_SBLK);
        if (img != null)
        {
            if (s.equals("SEMI_AUTOMATIC")) img.setColorFilter(ContextCompat.getColor(this,R.color.accent_light_color));
            else img.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
        }

        // Manual (Jog, etc.)
        img = (ImageView)findViewById(R.id.ControllerMode_Manaul);
        if (img != null)
        {
            if (s.equals("MANUAL")) img.setColorFilter(ContextCompat.getColor(this,R.color.accent_light_color));
            else img.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
        }

        // Edit
        img = (ImageView)findViewById(R.id.ControllerMode_Edit);
        if (img != null)
        {
            if (s.equals("EDIT")) img.setColorFilter(ContextCompat.getColor(this,R.color.accent_light_color));
            else img.setColorFilter(ContextCompat.getColor(this,R.color.foreground_light_color));
        }
    }

    /**
     * 更新程序执行状态
     * @param status 状态
     */
    private void updateControllerStatus_ExecutionMode(DeviceStatus status) {

        TextView txt = (TextView)findViewById(R.id.ExecutionModeText);
        if (txt != null) {

            txt.setText(status.mControllerInfo.executionMode);

            if (status.mControllerInfo.executionMode.equals("ACTIVE"))

                txt.setTextColor(ContextCompat.getColor(this,R.color.statusGreen));

            else if (status.mControllerInfo.executionMode.equals("STOPPED") || status.mControllerInfo.executionMode.equals("INTERRUPTED"))

                txt.setTextColor(ContextCompat.getColor(this,R.color.statusRed));

            else

                txt.setTextColor(ContextCompat.getColor(this,R.color.foreground_normal));
        }
    }

    /**
     * 更新控制器状态，含SystemStatus
     * @param status 状态
     */
    private void updateControllerStatus_SystemStatus(DeviceStatus status) {

        // Set System Status
        TextView txt = (TextView)findViewById(R.id.SystemStatusText);
        if (txt != null) {

            String s1 = status.mControllerInfo.systemStatus;

            txt.setText(s1);

            if (s1.equals("NORMAL"))

                txt.setTextColor(ContextCompat.getColor(this,R.color.statusGreen));

            else if (s1.equals("FAULT"))

                txt.setTextColor(ContextCompat.getColor(this,R.color.statusRed));

            else

                txt.setTextColor(ContextCompat.getColor(this,R.color.foreground_normal_color));


            // Set System Message
            txt = (TextView)findViewById(R.id.SystemMessageText);
            if (txt != null) {

                String s2 = status.mControllerInfo.systemMessage;

                // Set System Message Text
                txt.setText(s2);

                // Set Visibility of System Message
                if (s2 == null || s2.isEmpty()) txt.setVisibility(View.GONE);
                else txt.setVisibility(View.VISIBLE);

                // Set Colors
                if (!s1.equals("NORMAL")) {

                    txt.setTextColor(Color.WHITE);
                    txt.setBackgroundColor(Color.RED);

                } else {

                    txt.setTextColor(ContextCompat.getColor(this,R.color.foreground_normal_color));
                    txt.setBackgroundColor(Color.YELLOW);
                }
            }
        }
    }

    /**
     * 更新当前程序名
     * @param status 状态
     */
    private void updateControllerStatus_CurrentProgram(DeviceStatus status) {

        if (status.mControllerInfo != null) {

            // Set System Status
            String s = status.mControllerInfo.programName;
            TextView txt = (TextView)findViewById(R.id.CurrentProgramText);
            txt.setTextColor(ContextCompat.getColor(this,R.color.foreground_normal));
            if (txt != null) txt.setText(s);

        } else clearControllerStatus_CurrentProgram();
    }

    /**
     * 清除控制器程序名
     */
    private void clearControllerStatus_CurrentProgram() {

        TextView txt = (TextView)findViewById(R.id.CurrentProgramText);
        if (txt != null) txt.setText("");
    }

    /**
     * 更新进给
     * @param status 状态
     */
    private void updateControllerStatus_PathFeedRate(DeviceStatus status) {

        if (status.mControllerInfo!=null){

            TextView pathFeedRateView= (TextView) findViewById(R.id.pathFeedRate);
            if (pathFeedRateView!=null){
                pathFeedRateView.setText(status.mControllerInfo.pathFeedRate);
            }
        }else {
            clearControllerStatus_PathFeedRate();
        }
    }

    /**
     * 清除进给
     */
    private void clearControllerStatus_PathFeedRate() {

        TextView pathFeedRateView= (TextView) findViewById(R.id.pathFeedRate);
        if (pathFeedRateView!=null){
            pathFeedRateView.setText("");
        }
    }


    public void refresh() {

        if (deviceStatus != null) new GetDeviceStatus(this, deviceStatus.uniqueId).execute();
    }


    /*
    public void logout(){

        Loading.Open(this, "Logging Out..");

        new Logout(this).execute();
    }
    */

    //region Loading Overlay

    public void showLoading() {

        View v = findViewById(R.id.loadingOverlay);
        if (v != null) {

            v.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {

        View v = findViewById(R.id.loadingOverlay);
        if (v != null) {

            v.setVisibility(View.GONE);
        }
    }

    //endregion

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

    //region Toolbar

    private void setToolbar() {

        // Set Toolbar
        Toolbar deviceMonitorToolbar = (Toolbar) findViewById(R.id.DeviceMonitorToolbar);

        if (deviceMonitorToolbar!=null){

            // Set Title
            deviceMonitorToolbar.setTitle("Device Details");
            deviceMonitorToolbar.setTitleTextColor(Color.WHITE);

            // Set Icon
            deviceMonitorToolbar.setLogo(R.drawable.th_logo_toolbar);

            toolbar = deviceMonitorToolbar;

            setSupportActionBar(deviceMonitorToolbar);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_refresh) {

            refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Navigation Drawer

    private void setNavigationDrawer() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {

            apiServerTextView= (TextView) headerView.findViewById(R.id.ApiServer);
            updateIntervalTextView= (TextView) headerView.findViewById(R.id.UpdateInterval);

            if (apiServerTextView!=null){

                if (ApiConfiguration.getSaveHost(this)!=null){

                    apiServerTextView.setText(ApiConfiguration.getSaveHost(this));
                }else {

                    apiServerTextView.setText(ApiConfiguration.apiHost.toString());
                }
            }


            if (updateIntervalTextView!=null){

                if (ApiConfiguration.getUpdateInterval(this)>=0){

                    updateIntervalTextView.setText(String.valueOf(ApiConfiguration.getUpdateInterval(this)));
                }else {

                    updateIntervalTextView.setText(String.valueOf(ApiConfiguration.apiUpdateInterval));
                }
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {

            // Open the About Page
            startActivity(new Intent(getBaseContext(), About.class));

        } else if (id == R.id.nav_deviceList) {

            // Open the Device Details Page
            startActivity(new Intent(getBaseContext(), DeviceList.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //endregion
}
