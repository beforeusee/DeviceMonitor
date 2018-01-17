package com.example.zhengxiaohu.devicemonitor.device_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhengxiaohu.devicemonitor.R;
import com.example.zhengxiaohu.devicemonitor.api.data.DescriptionInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.OeeInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.StatusInfo;

import org.joda.time.Period;

import java.util.ArrayList;

/**
 * Created by XiaoHu Zheng on 2017/12/24.
 * Email: 1050087728@qq.com
 */

public class ListAdapter extends ArrayAdapter<ListItem> {

    private Context context;

    public ListAdapter(Context context, ArrayList<ListItem> listItems){
        super(context,0,listItems);
        this.context=context;
    }

    private static class ViewHolder{

        //Description
        TextView Description;
        TextView DeviceId;
        TextView Manufacturer;
        TextView Model;
        TextView Serial;

        ImageView Logo;
        ImageView Image;

        //Status
        View StatusIndicator;
        ImageView StatusIcon;

        TextView ProductionStatus;
        TextView ProductionStatusTimer;

        TextView OEE;

        public int StatusUnavailable;
        public int StatusRed;
        public int StatusOrange;
        public int StatusGreen;
        public int ForegroundNormal;

        View OeeLayout;
        View SelectionArrow;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //get the data item for this position
        ListItem item=getItem(position);

        //check if an existing view is being reused,otherwise inflate the view
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.device_item,parent,false);

            holder=new ViewHolder();

            initializeColors(convertView,holder);
            initializeDescription(convertView,holder);
            initializeStatusIndicator(convertView,holder);
            initializeProductionStatus(convertView,holder);
            initializeOEEStatus(convertView,holder);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

//        setUserType(holder);

        //set the description components
        setDescription(holder,item.descriptionInfo);

        //set the status indicator color
        setStatusIndicator(holder,item.statusInfo);

        //set production status info
        setProductionStatus(holder,item.statusInfo);

        //set OEE Status Info
        setOEEStatus(holder,item.oeeInfo);

        //return the completed view to render on screen
        return convertView;
    }

    private void initializeColors(View view, ViewHolder holder) {

        holder.StatusUnavailable = ContextCompat.getColor(view.getContext(), R.color.disabled);
        holder.StatusGreen = ContextCompat.getColor(view.getContext(), R.color.statusGreen);
        holder.StatusRed = ContextCompat.getColor(view.getContext(), R.color.statusRed);
        holder.StatusOrange = ContextCompat.getColor(view.getContext(), R.color.statusOrange);

        holder.ForegroundNormal = ContextCompat.getColor(view.getContext(), R.color.foreground_normal_color);

        View v = view.findViewById(R.id.Arrow);
        if (v != null) holder.SelectionArrow = v;
    }

    private void initializeDescription(View view, ViewHolder holder) {

        View v;

        // Description
        v = view.findViewById(R.id.Description);
        if (v != null) holder.Description = (TextView)v;

        // Device Id
        v = view.findViewById(R.id.DeviceId);
        if (v != null) holder.DeviceId = (TextView)v;

        // Manufacturer
        v = view.findViewById(R.id.Manufacturer);
        if (v != null) holder.Manufacturer = (TextView)v;

        // Model
        v = view.findViewById(R.id.Model);
        if (v != null) holder.Model = (TextView)v;

        // Serial
        v = view.findViewById(R.id.Serial);
        if (v != null) holder.Serial = (TextView)v;

        // Logo
//        v = view.findViewById(R.id.ManufacturerLogo);
//        if (v != null) holder.Logo = (ImageView)v;
    }

    private void setDescription (ViewHolder holder, DescriptionInfo info) {

        if (info != null) {

            if (holder.Description != null) holder.Description.setText(info.description);
            if (holder.DeviceId != null) holder.DeviceId.setText(info.deviceId);
            if (holder.Manufacturer != null) holder.Manufacturer.setText(info.manufacturer);
            if (holder.Model != null) holder.Model.setText(info.model);
            if (holder.Serial != null) holder.Serial.setText(info.serial);
        }
    }

    private void initializeStatusIndicator(View view, ViewHolder holder) {

        View v;

        // Status Indicator
        v = view.findViewById(R.id.StatusIndicator);
        if (v != null) holder.StatusIndicator = v;

        // Alert Icon
        v = view.findViewById(R.id.StatusIcon);
        if (v != null) holder.StatusIcon = (ImageView)v;
    }

    private void setStatusIndicator(ViewHolder holder, StatusInfo info) {

        if (holder.StatusIndicator != null && holder.StatusIcon != null && info != null && info.deviceStatus != null) {

            if (!info.connected) {

                holder.StatusIndicator.setBackgroundColor(holder.StatusUnavailable);
                holder.StatusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.power_01_white));

            } else {

                if (info.deviceStatus.equals(("Alert"))) {

                    holder.StatusIndicator.setBackgroundColor(holder.StatusRed);
                    holder.StatusIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.warning_01));

                } else if (info.deviceStatus.equals(("Idle"))) {

                    holder.StatusIndicator.setBackgroundColor(holder.StatusOrange);
                    holder.StatusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.hourglass_01_white));

                } else if (info.deviceStatus.equals("Active")) {

                    holder.StatusIndicator.setBackgroundColor(holder.StatusGreen);
                    holder.StatusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.active_01_white));
                }
            }
        }
    }

    private void initializeProductionStatus(View view, ViewHolder holder) {

        View v;

        // Production Status
        v = view.findViewById(R.id.DeviceStatus);
        if (v != null) holder.ProductionStatus = (TextView)v;

        // Production Status Timer
        v = view.findViewById(R.id.ProductionStatusTimer);
        if (v != null) holder.ProductionStatusTimer = (TextView)v;
    }

    private void setProductionStatus(ViewHolder holder, StatusInfo info) {

        if (info != null && info.deviceStatus != null) {

            if (holder.ProductionStatus != null) holder.ProductionStatus.setText(info.deviceStatus);

            if (holder.ProductionStatusTimer != null && info.deviceStatusTimer != null) {

                Period period = new Period(info.deviceStatusTimer * 1000);
                String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());

                holder.ProductionStatusTimer.setText(statusPeriod);
            }
        }
    }

    private void initializeOEEStatus(View view, ViewHolder holder) {

        View v;

        v = view.findViewById(R.id.OeeLayout);
        if (v != null) holder.OeeLayout = v;

        // OEE
        v = view.findViewById(R.id.OEE);
        if (v != null) holder.OEE = (TextView)v;
    }

    private void setOEEStatus(ViewHolder holder, OeeInfo info) {

        // Set OEE
        if (holder.OEE != null && info != null) {

            double d = info.oee * 100;
            String s = String.format("%.0f%%",d);
            holder.OEE.setText(s);

            if (info.oee > 0.6)
                holder.OEE.setTextColor(holder.StatusGreen);
            else if (info.oee > 0.3)
                holder.OEE.setTextColor(holder.ForegroundNormal);
            else
                holder.OEE.setTextColor(holder.StatusRed);
        }
    }

    /*
    private void setUserType(ViewHolder holder) {

        // Selection Arrow
        if (holder.SelectionArrow != null) {

            if (MyApplication.User != null && MyApplication.User.type > 0) holder.SelectionArrow.setVisibility(View.VISIBLE);
            else holder.SelectionArrow.setVisibility(View.GONE);
        }

        // Oee
        if (holder.OeeLayout != null) {

            if (MyApplication.User != null && MyApplication.User.type > 0) holder.OeeLayout.setVisibility(View.VISIBLE);
            else holder.OeeLayout.setVisibility(View.GONE);
        }
    }
    */
}
