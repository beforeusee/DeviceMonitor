// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package com.example.zhengxiaohu.devicemonitor.devices;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.zhengxiaohu.devicemonitor.api.http.Requests;

/**
 * Created by XiaoHu Zheng on 4/27/2016.
 * Email: 1050087728@qq.com
 */
public class Device {

//    // Table Addresses ------------------------------------------------------------------
//
//    // General
//    private static String ADR_UNIQUE_ID = "device_unique_id";
//    private static String ADR_ENABLED = "device_client_enabled";
//
//    // Description
//    private static String ADR_DESCRIPTION = "device_description";
//    private static String ADR_MANUFACTURER = "device_manufacturer";
//    private static String ADR_DEVICE_ID = "device_device_id";
//    private static String ADR_MODEL = "device_model";
//    private static String ADR_SERIAL = "device_serial";
//    private static String ADR_CONTROLLER = "device_controller";
//
//    // Images
//    private static String ADR_LOGO_URL = "device_logo_url";
//    private static String ADR_IMAGE_URL = "device_image_url";
//
//    // ----------------------------------------------------------------------------------


//    public DeviceStatus Status;

    public String UniqueId;
    public Boolean Enabled;

    // Description
    public String Description;
    public String Device_Id;
    public String Manufacturer;
    public String Model;
    public String Serial;
    public String Controller;

    // Manufacturer Logo
    public String LogoUrl;
    public Bitmap Logo;

    // Device Image
    public String ImageUrl;
    public Bitmap Image;

    public Device() {

//        Status = new DeviceStatus();

    }

    public static Device parse(JSONObject json) {

        Device result = null;

        if (json != null) {

            result = new Device();

            try {

                result.UniqueId = json.getString("unique_id");
                result.Description = json.getString("description");
                result.Device_Id = json.getString("device_id");
                result.Manufacturer = json.getString("manufacturer");
                result.Model = json.getString("model");
                result.Serial = json.getString("serial");
                result.Controller = json.getString("controller");

                result.ImageUrl = json.getString("image_url");
                result.LogoUrl = json.getString("logo_url");
            }
            catch (JSONException ex) { ex.getStackTrace(); }
            catch (Exception ex) { ex.getStackTrace(); }
        }

        return result;
    }

    public static void setLogoImage(Device device) {

        if (device.LogoUrl != null) {

            String url = "http://www.feenux.com/trakhound/users/files/" + device.LogoUrl;

            Bitmap logo = Requests.getImage(url);
            if (logo != null) {

                device.Logo = logo;
            }
        }
    }

    private static void setDeviceImage(Device device) {

        if (device.ImageUrl != null) {

            String url = "http://www.feenux.com/trakhound/users/files/" + device.ImageUrl;

            Bitmap img = Requests.getImage(url);
            if (img != null) {

                device.Image = img;
            }
        }
    }

}
