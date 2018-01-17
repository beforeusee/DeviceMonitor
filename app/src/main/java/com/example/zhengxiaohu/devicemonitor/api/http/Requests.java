package com.example.zhengxiaohu.devicemonitor.api.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by XiaoHu Zheng on 2017/12/23.
 * Email: 1050087728@qq.com
 */

public class Requests {
    public static String POST="POST";
    public static String GET="GET";


    public static String post(String url,PostData[] postDatas){
        return runRequest(url,postDatas,POST,3);
    }

    public static String get(String url){
        return runRequest(url,null,GET,3);
    }

    private static String runRequest(String url, PostData[] postDatas, String method, int maxAttempts) {
        String result = null;

        //Format PostData Array
        String postData = formatPostData(postDatas);

        InputStream stream = null;
        Boolean success = false;
        int i = 0;
        while (!success && i < maxAttempts) {

            try {

                HttpURLConnection conn = getConnection(url, method);
                if (conn != null) {

                    if (postData != null) {

                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        wr.write(postData);
                        wr.flush();
                        wr.close();
                    }

                    stream = conn.getInputStream();
                    result = readTextStream(stream);

                    conn.disconnect();

                    success = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Delay a little before next request attempt
            if (!success) {
                try {
                    Thread.sleep(250);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            i++;
        }
        return result;
    }

    private static String readTextStream(InputStream stream) throws IOException {

        String result=null;
        if (stream != null) {

            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader=new BufferedReader(streamReader);

            StringBuilder stringBuilder=new StringBuilder();
            String line=null;

            //build response string
            try {
                while ((line=reader.readLine())!=null){
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            result=stringBuilder.toString();

            //close IO Readers
            reader.close();
            streamReader.close();
        }
        return result;
    }

    private static HttpURLConnection getConnection(String url, String method) {

        try {

            //Open Connection
            URL u=new URL(url);

            HttpURLConnection conn=(HttpURLConnection) u.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod(method);

            conn.setDoInput(true);
            conn.setDoOutput(method.equals(POST));

            conn.connect();

            return conn;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String formatPostData(PostData[] postDatas) {

        String result=null;
        if (postDatas!=null){
            result="";
            for (int i=0;i<postDatas.length;i++){
                result+=encodePostData(postDatas[i]);
                if (i<postDatas.length-1){
                    result+="&";
                }
            }
        }
        return result;
    }

    private static String encodePostData(PostData postData) {

        String result=null;
        try {
            String id=postData.id;
            String value=postData.value;

            result= URLEncoder.encode(id,"UTF-8")+"="+URLEncoder.encode(value,"UTF-8");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static Bitmap getImage(String url) {

        try {

            HttpURLConnection conn = getConnection(url, GET);

            InputStream input = null;
            if (conn != null) {
                input = conn.getInputStream();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            if (conn != null) {
                conn.disconnect();
            }

            return bitmap;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}
