package com.example.zhengxiaohu.devicemonitor.tools;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoHu Zheng on 2018/4/8.
 * Email: 1050087728@qq.com
 */

public class MPAndroidLineChartManager {

    private static String lineName=null;
    private static String lineName1=null;
    private static String lineName2=null;
    private static String lineName3=null;

    private int count=0;
    private int mXAxisMin=0;
    private Context mContext;
    /**
     * 单条曲线
     * @param context 上下文
     * @param lineChart 图标
     * @param x x轴数据集
     * @param y y轴数据集
     * @param label 标签
     * @return 数据
     */
    public static LineData initSingleLineChart(Context context, LineChart lineChart, List<Float> x, List<Float> y, String label){


        //x轴数据
        //y轴数据
        List<Entry> yValues=new ArrayList<>();

        for (int i=0;i<x.size();i++){
            yValues.add(new Entry(x.get(i),y.get(i)));
        }

        //创建LineDataSet
        LineDataSet dataSet=new LineDataSet(yValues,label);
        //设置LineDataSet的样式
        //设置线宽
        dataSet.setLineWidth(2.0f);
        //设置显示的圆形大小
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        //折线显示的颜色
        dataSet.setColor(Color.BLUE);
        //高亮的线的颜色
        dataSet.setHighLightColor(Color.GREEN);
        dataSet.setHighlightEnabled(true);


        //创建LineData
        LineData lineData=new LineData(dataSet);
        return lineData;
    }

    public static void initChartStyle(Context context, LineChart lineChart, LineData lineData){

        //设置点击折线点时，显示其数值
        Description description=new Description();
        description.setText("");
        description.setTextSize(12.0f);
        description.setTextColor(Color.GREEN);

        lineChart.setDescription(description);

        //折线图上加边框
        lineChart.setDrawBorders(true);
        //表格颜色
        lineChart.setDrawGridBackground(true);
        lineChart.setGridBackgroundColor(Color.WHITE);
        //设置是否可点击
        lineChart.setTouchEnabled(true);
        //设置可拖曳
        lineChart.setDragEnabled(true);
        //设置可缩放
        lineChart.setScaleEnabled(true);
        //如果设置为true，则启用捏缩放。 如果禁用，x轴和y轴可以分开放大。
        lineChart.setPinchZoom(true);
        //设置背景颜色
        lineChart.setBackgroundColor(Color.WHITE);

        lineChart.setData(lineData);

        //设置图例
        Legend legend=lineChart.getLegend();
        //图例样式为方形
        legend.setForm(Legend.LegendForm.LINE);
        //图例字体大小
        legend.setFormSize(12f);
        //图例文字颜色灰色
        legend.setTextColor(Color.GRAY);

        //x轴可显示的坐标范围
//        lineChart.setVisibleXRange(0,720);
        //x轴的标志
        XAxis xAxis=lineChart.getXAxis();

        //x轴位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //x轴字体的颜色
        xAxis.setTextColor(Color.GRAY);
        //x轴字体大小
        xAxis.setTextSize(12f);
        //网格线颜色
        xAxis.setGridColor(Color.GRAY);
        //显示网格线
        xAxis.setDrawAxisLine(true);
        //x轴的图例
        xAxis.setDrawLabels(true);

        //y轴左边坐标标志
        YAxis yAxisLeft=lineChart.getAxisLeft();
        //y轴右边坐标标志
        YAxis yAxisRight=lineChart.getAxisRight();
        //y轴左边字体颜色,大小
        yAxisLeft.setTextColor(Color.GRAY);
        yAxisLeft.setTextSize(12f);
        //y轴是否显示网格
        yAxisLeft.setLabelCount(5,true);
        yAxisLeft.setGridColor(Color.GRAY);

        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawLabels(false);

        //动画效果
//        lineChart.animateY(50, Easing.EasingOption.Linear);
//        lineChart.animateX(2000, Easing.EasingOption.Linear);
        //渲染
        lineChart.invalidate();
    }


    public static String getLineName() {
        return lineName;
    }

    /**
     * 设置折线名称
     * @param lineName name
     */
    public static void setLineName(String lineName) {
        MPAndroidLineChartManager.lineName = lineName;
    }

    public static String getLineName1() {
        return lineName1;
    }

    /**
     * 设置折线名称
     * @param lineName1 name1
     */
    public static void setLineName1(String lineName1) {
        MPAndroidLineChartManager.lineName1 = lineName1;
    }

    public static String getLineName2() {
        return lineName2;
    }

    /**
     * 设置另一条折线名称
     * @param lineName2 name2
     */
    public static void setLineName2(String lineName2) {
        MPAndroidLineChartManager.lineName2 = lineName2;
    }

    public static String getLineName3() {
        return lineName3;
    }

    public static void setLineName3(String lineName3) {
        MPAndroidLineChartManager.lineName3 = lineName3;
    }
}
