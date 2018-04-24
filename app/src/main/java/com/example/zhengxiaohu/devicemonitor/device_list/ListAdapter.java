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
import com.example.zhengxiaohu.devicemonitor.api.data.machine.DescriptionInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.OeeInfo;
import com.example.zhengxiaohu.devicemonitor.api.data.machine.StatusInfo;

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
        TextView Address;
        TextView Port;
        TextView Name;
        TextView Uuid;
        TextView DeviceId;
//        TextView Manufacturer;
//        TextView Model;
//        TextView Serial;

        //Status
        View StatusIndicator;
        ImageView StatusIcon;

        public int StatusUnavailable;
        public int StatusRed;
        public int StatusOrange;
        public int StatusGreen;
        public int ForegroundNormal;

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

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

//        setUserType(holder);

        //set the description components
        setDescription(holder,item.mDescriptionInfo);

        //set the status indicator color
        setStatusIndicator(holder,item.mStatusInfo);

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
        v = view.findViewById(R.id.Address);
        if (v != null) holder.Address = (TextView)v;

        v=view.findViewById(R.id.Port);
        if (v!=null) holder.Port= (TextView) v;

        v=view.findViewById(R.id.Name);
        if (v!=null) holder.Name= (TextView) v;

        v=view.findViewById(R.id.Uuid);
        if (v!=null) holder.Uuid= (TextView) v;

        // Device Id
        v = view.findViewById(R.id.DeviceId);
        if (v != null) holder.DeviceId = (TextView)v;
    }

    private void setDescription (ViewHolder holder, DescriptionInfo info) {

        if (info != null) {

            if (holder.Address!=null) holder.Address.setText(info.address);
            if (holder.Port!=null) holder.Port.setText(info.port);
            if (holder.Name!=null) holder.Name.setText(info.name);
            if (holder.Uuid!=null) holder.Uuid.setText(info.uuid);
            if (holder.DeviceId != null) holder.DeviceId.setText(info.deviceId);
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
                holder.StatusIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.power_01_white));

            } else {

                holder.StatusIndicator.setBackgroundColor(holder.StatusGreen);
                holder.StatusIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.power_01_white));
            }
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
