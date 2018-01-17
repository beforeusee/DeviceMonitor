package com.example.zhengxiaohu.devicemonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhengxiaohu.devicemonitor.device_list.*;
import com.example.zhengxiaohu.devicemonitor.device_details.GetDeviceStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class DeviceList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView deviceListView;
    private ListAdapter listAdapter;

    Thread statusThread;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        MyApplication.setCurrentActivity(this);

        context=this;
        deviceListView= (ListView) findViewById(R.id.device_list);
        listAdapter=new ListAdapter(this,new ArrayList<ListItem>());
        deviceListView.setAdapter(listAdapter);

        //Set onClick listener
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //加载页面
                //Loading.Open(context,"Loading Details..");

                //获取设备状态的异步任务
                new GetDeviceStatus(context,position).execute();
            }
        });

        //Set Status Bar Color
        setStatusBar();

        //Setup Toolbar/Action Bar
        setToolbar();

        //setup navigation drawer
        setNavigationDrawer();

        //Load Devices
        ListItem[] listItems=MyApplication.ListItems;
        if (listItems==null){
            loadDevices();
        }else {
            addDevices();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (statusThread!=null){
            statusThread.interrupt();
        }
    }

    /**
     * 加载设备列表
     */
    private void loadDevices(){
        // show Loading Activity
        Loading.open(this,"Loading Devices..");

        //Get Devices
        new GetDevices(this).execute();
    }

    /**
     * refresh Devices
     */
    private void refresh(){

        //show Loading Overlay
        showLoading();

        //get Devices
        new GetDevices(this).execute();
    }

    public void updateStatus(DeviceStatus[] deviceStatus){

        final DeviceStatus[] statuses=deviceStatus;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    for (int i=0;i<statuses.length;i++){

                        DeviceStatus status=statuses[i];
                        String uniqueId=status.uniqueId;

                        int listIndex=getListIndex(uniqueId);
                        if (listIndex>=0){
                            ListItem listItem=listAdapter.getItem(listIndex);
                            listItem.statusInfo=status.statusInfo;
                            listItem.oeeInfo=status.oeeInfo;
                        }
                    }

                    listAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private int getListIndex(String uniqueId) {

        ListItem[] listItems = MyApplication.ListItems;
        if (listItems != null) {

            for (int i = 0; i < listItems.length; i++) {

                if (listItems[i].uniqueId.equals(uniqueId)) return i;
            }
        }

        return -1;
    }

    public void addDevices(){

        listAdapter.clear();

        ListItem[] listItems=MyApplication.ListItems;

        if (listItems!=null&&listItems.length>0){

            Arrays.sort(listItems, new Comparator<ListItem>() {
                @Override
                public int compare(ListItem o1, ListItem o2) {
                    if (o1.index>o2.index){
                        return 1;
                    }
                    if (o1.index<o2.index){
                        return -1;
                    }
                    return 0;
                }
            });

            listAdapter.addAll(listItems);

            //hide "No Devices Found" text
            View view=findViewById(R.id.NoDevicesText);
            if (view!=null){
                view.setVisibility(View.GONE);
            }
        }else {
            //show "No Devices Found" text
            View view=findViewById(R.id.NoDevicesText);
            if (view!=null){
                view.setVisibility(View.VISIBLE);
            }
        }

        if (statusThread==null){
            startStatusThread();
        }

        hideLoading();

    }

    private void startStatusThread(){

        //start status updates on separate thread
        StatusHandler statusHandler=new StatusHandler(this);

        statusThread=new Thread(statusHandler);

        statusThread.start();
    }

    private boolean connected;

    public void updateConnectionStatus(boolean connected){
        this.connected=connected;
    }

    //region Loading Overlay
    public void showLoading(){

        // show loading
        View v=findViewById(R.id.loadingOverlay);
        if (v!=null){
            v.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading(){

        // hide loading
        Loading.close();

        View v=findViewById(R.id.loadingOverlay);
        if (v!=null){
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
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar_color));
        }
    }

    //region Toolbar

    private void setToolbar(){

        //set Toolbar
        Toolbar deviceMonitorToolbar= (Toolbar) findViewById(R.id.DeviceMonitorToolbar);

        //set title
        deviceMonitorToolbar.setTitle("Device List");
        deviceMonitorToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.toolbar_title_color));

        //set icon
        deviceMonitorToolbar.setLogo(R.drawable.th_logo_toolbar);

        toolbar=deviceMonitorToolbar;
        setSupportActionBar(deviceMonitorToolbar);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 创建菜单
     * @param menu menu
     * @return true or false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate the menu ,this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.device_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle action bar item clicks here. the action bar will
        //automatically handle clicks on the Home/Up button  so long
        //as you specify a parent activity in AndroidManifest.xml.
        int id=item.getItemId();

        if (id==R.id.action_refresh){
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Navigation Drawer
    private void setNavigationDrawer(){

        DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //handle navigation view item clicks here
        int id=item.getItemId();

        if (id==R.id.nav_about){

            //open the About page
            startActivity(new Intent(getBaseContext(),About.class));
        }

        if (id==R.id.nav_settings){

            //open the Settings page
            startActivity(new Intent(getBaseContext(),Settings.class));
        }
        DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //endregion

}
