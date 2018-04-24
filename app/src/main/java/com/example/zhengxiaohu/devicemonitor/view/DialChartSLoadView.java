package com.example.zhengxiaohu.devicemonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import org.xclcharts.chart.DialChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotAttrInfo;
import org.xclcharts.view.GraphicalView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoHu Zheng on 2018/1/24.
 * Email: 1050087728@qq.com
 */

public class DialChartSLoadView extends GraphicalView {

    private static final String TAG = "DialChartSLoadView";
    private DialChart mChart=new DialChart();
    private float mPercentage=0.0f;

    public DialChartSLoadView(Context context){
        super(context);

        initView();
    }

    public DialChartSLoadView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        initView();
    }

    public DialChartSLoadView(Context context, AttributeSet attributeSet, int defStyle){
        super(context,attributeSet,defStyle);
        initView();
    }

    private void initView() {
        chartRender();
    }

    private void chartRender() {

        try {

            //设置背景色
            mChart.setApplyBackgroundColor(true);
            //LightGrey 浅灰色
            mChart.setBackgroundColor(Color.rgb(211,211,211));

            //绘制边框
//            mChart.showRoundBorder();

            //设置指针长度
            mChart.getPointer().setLength(0.5f);

            //增加轴
            addAxis();

            //增加附加信息
            addAttrInfo();

            mChart.getPointer().setPercentage(mPercentage);

            //深橙色
            mChart.getPointer().getPointerPaint().setColor(Color.rgb(255,165,0));
            mChart.getPointer().getBaseCirclePaint().setColor(Color.WHITE);

        }catch (Exception ex){
            Log.d(TAG, "chartRender: "+ex.toString());
        }
    }


    private void addAxis() {

        //开始设置轴
        //轴1--最外面的弧线轴
        mChart.addArcLineAxis(1f);

        //轴2--环形颜色轴
        setStrokeRingAxisByPercentage(mPercentage);

        //设置直线坐标轴(TOP,LEFT,RIGHT)
        mChart.addLineAxis(XEnum.Location.TOP,0.3f);
        mChart.addLineAxis(XEnum.Location.LEFT,0.3f);
        mChart.addLineAxis(XEnum.Location.RIGHT,0.3f);

        //设置直线坐标轴的颜色和宽度
        //设置TOP轴
        if (mChart.getPlotAxis().size()>=2){
            mChart.getPlotAxis().get(2).getAxisPaint().setColor(Color.rgb(255,215,0));
            mChart.getPlotAxis().get(2).getAxisPaint().setStrokeWidth(5);
        }

        //设置LEFT轴
        if (mChart.getPlotAxis().size()>=3){
            mChart.getPlotAxis().get(3).getAxisPaint().setColor(Color.rgb(50,205,50));
            mChart.getPlotAxis().get(3).getAxisPaint().setStrokeWidth(5);
        }

        //设置RIGHT轴
        if (mChart.getPlotAxis().size()>=4){
            mChart.getPlotAxis().get(4).getAxisPaint().setColor(Color.rgb(255,69,0));
            mChart.getPlotAxis().get(4).getAxisPaint().setStrokeWidth(5);
        }

        //设置最外圈的环形轴的颜色和宽度
        mChart.getPlotAxis().get(0).getAxisPaint().setColor(Color.rgb(211,211,211));
        mChart.getPlotAxis().get(0).getAxisPaint().setStrokeWidth(2);

        //设置环形轴内圈到圆心之间的填充色,为浅灰色LightGrey
        mChart.getPlotAxis().get(1).getFillAxisPaint().setColor(Color.rgb(211,211,211));

        //Pink--粉红色
        mChart.addCircleAxis(0.2f,Color.rgb(255,192,203));
    }

    private void setStrokeRingAxisByPercentage(float percentage){

        //根据percentage的大小来设置圆环的显示比例和颜色
        List<Float> ringPercentage=new ArrayList<>();
        List<Integer> rColor=new ArrayList<>();

        //圆环各段的末端位置定义
        float greenEndPos=0.64286f;
        float yellowEndPos=0.82143f;
        float redEndPos=1.0f;

        //定义酸橙绿色，金色，橙红色，背景白灰色
        int green=Color.rgb(50,205,50);
        int yellow=Color.rgb(255,215,0);
        int red=Color.rgb(255,69,0);
        int background=Color.rgb(245,245,245);

        //只显示绿色部分
        if (percentage>=0&&percentage<=greenEndPos){

            ringPercentage.add(percentage);
            ringPercentage.add(redEndPos-percentage);

            //添加绿色和背景色(白烟)
            rColor.add(green);
            rColor.add(background);
        }
        //显示绿色和黄色
        if (percentage>greenEndPos&&percentage<=yellowEndPos){

            ringPercentage.add(greenEndPos);
            ringPercentage.add(percentage-greenEndPos);
            ringPercentage.add(redEndPos-percentage);

            //添加绿色，黄色和背景色(白烟)
            rColor.add(green);
            rColor.add(yellow);
            rColor.add(background);
        }

        //显示绿色，黄色与红色
        if (percentage>yellowEndPos){

            ringPercentage.add(greenEndPos);
            ringPercentage.add(yellowEndPos-greenEndPos);
            ringPercentage.add(percentage-yellowEndPos);
            ringPercentage.add(redEndPos-percentage);

            //添加绿色,黄色,红色和背景色(白烟)
            rColor.add(green);
            rColor.add(yellow);
            rColor.add(red);
            rColor.add(background);
        }

        mChart.setStartAngle(130);
        mChart.setTotalAngle(280);
        mChart.addStrokeRingAxis(0.8f,0.5f,ringPercentage,rColor);
    }

    private void addAttrInfo() {

        PlotAttrInfo plotAttrInfo = mChart.getPlotAttrInfo();

        //设置附加信息
        Paint paintBT = new Paint();
        paintBT.setColor(Color.GRAY);
        paintBT.setTextAlign(Paint.Align.CENTER);
        paintBT.setTextSize(37);

        plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM,String.format("%.1f",mPercentage*100)+"%", 0.8f, paintBT);
    }

    @Override
    public void render(Canvas canvas) {

        try {
            mChart.render(canvas);
        } catch (Exception e) {
            Log.d(TAG, "render: "+e.toString());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChart.setChartRange(w,h);
    }

    public void setCurrentStatus(float percentage){

        //清理
        mChart.clearAll();

        mPercentage=percentage;
        //设置当前百分比
        mChart.getPointer().setPercentage(mPercentage);

        addAxis();
        addAttrInfo();
    }
}
