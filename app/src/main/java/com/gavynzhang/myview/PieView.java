package com.gavynzhang.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gavynzhang.myview.model.Pie;
import com.gavynzhang.myview.model.PieHeadEnd;

/**
 * Created by GavynZhang on 2016/8/5.
 */
public class PieView extends View {

    private Paint mPaint;
    private Rect mRect;

    //各块数据
    private Pie[] pies;
    //各块首尾
    private PieHeadEnd[] pieHeadEnds;
    //初始颜色
    private int initalColor;
    //大圆半径
    private float radius;
    //圆角偏转角
    private int chamferingAngle;
    //描边宽度
    private int strokeWidth;
    //内圆半径
    private float innerCircleRadius;
    //内圆颜色
    private int innerColor;
    //百分比
    private String[] percentage;

    //测试内容
    private int textSize;


    private RectF rectF = null;
    private Path path = null;


    public PieView(Context context) {
        super(context);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PieView);
        initalColor = a.getColor(R.styleable.PieView_inital_color,0);
        radius = a.getDimension(R.styleable.PieView_pie_radius, 100);
        chamferingAngle = a.getInteger(R.styleable.PieView_pie_chamfering_angle, 2);
        strokeWidth = a.getInteger(R.styleable.PieView_stroke_width,3);
        innerCircleRadius = a.getDimension(R.styleable.PieView_inner_circle_radius,100);
        innerColor = a.getColor(R.styleable.PieView_inner_color, getResources().getColor(R.color.background));
        textSize = a.getInteger(R.styleable.PieView_text_size, 30);

        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        path = new Path();
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.CYAN);

        if(pies != null) {

            Log.w("Test", String.valueOf(pies[0].getNumber())+"/////"+String.valueOf(pies[0].getColor()));

            getPieHeadEnd();

            Log.w("Test", String.valueOf(pieHeadEnds[0].getHead())+"///"+pieHeadEnds[0].getEnd());


            for (int i = 0; i < pies.length; i++) {
                Log.w("Test", String.valueOf(pies.length));
                Log.w("Test", String.valueOf(pieHeadEnds.length));
                drawSector(canvas, pieHeadEnds[i].getHead(), pieHeadEnds[i].getEnd(), pies[i].getColor(), pies[i].getDeepColor(), pies[i].getDepartNum(),i);
                Log.w("Test", String.valueOf(pieHeadEnds[i].getHead()));
            }
        }else {

            Log.w("Test","No Data!");

        }


    }

    /**
     *
     * @param startAngle :起始角度
     * @param endAngle:终止角度
     * @param color:内部颜色
     * @param deepColor:描边颜色
     * @param departNum: 增加空隙离开像素
     *
     * */
    private void drawSector(Canvas canvas, float startAngle, float endAngle, int color, int deepColor,  int departNum, int i){

        //扇形角度
        float sectorAngle = endAngle - startAngle;
        //中间角度
        float midAngle = sectorAngle/2 + startAngle;
        //x偏移量
        float xDepartAmount = (float) (departNum*Math.cos(Math.toRadians(midAngle)));
        //y偏移量
        float yDepartAmount = (float) (departNum*Math.sin(Math.toRadians(midAngle)));

        //小圆（圆角）半径
        float minR = (float) (radius*Math.tan(Math.toRadians(chamferingAngle))*Math.tan(Math.toRadians((90f - chamferingAngle)/2)));

        //大扇形外接矩形坐标
        float maxRectLeft = getPaddingLeft()+xDepartAmount;
        float maxRectTop = getPaddingTop() + yDepartAmount;
        float maxRectRight = getPaddingLeft()+radius*2 + xDepartAmount;
        float maxRectBottom = getPaddingTop()+radius*2 +yDepartAmount;

        //大扇型圆心
        float bigCircleCenterX = getPaddingLeft() + radius + xDepartAmount;
        float bigCircleCenterY = getPaddingTop() + radius + yDepartAmount;

        //起始小圆与扇形起始边切点
        float startMinCircleCutDotX = (float) (bigCircleCenterX + minR / Math.tan(Math.toRadians(chamferingAngle)) * Math.cos(Math.toRadians(startAngle)) );
        float startMinCircleCutDotY = (float) (bigCircleCenterY + minR / Math.tan(Math.toRadians(chamferingAngle)) * Math.sin(Math.toRadians(startAngle)) );

        //结束小圆与扇形结束边切点
        float endMinCircleCutDotX = (float) (bigCircleCenterX - radius*(1-Math.tan(Math.toRadians(chamferingAngle))*Math.tan(Math.toRadians((90f-chamferingAngle)/2)))*Math.cos(Math.toRadians(chamferingAngle))*Math.sin(Math.toRadians(endAngle - 91f)));
        float endMinCircleCutDotY = (float) (bigCircleCenterY+radius*(1-Math.tan(Math.toRadians(chamferingAngle))*Math.tan(Math.toRadians((90f-chamferingAngle)/2)))*Math.cos(Math.toRadians(chamferingAngle))*Math.cos(Math.toRadians(endAngle - 91f)));

        //起始小圆圆心坐标
        float startMinCircleCenterX = (float) (bigCircleCenterX + (radius - minR) * Math.cos(Math.toRadians(chamferingAngle + startAngle)) );
        float startMinCircleCenterY = (float) (bigCircleCenterY +(radius - minR)* Math.sin(Math.toRadians(chamferingAngle + startAngle)) );

        //结束小圆圆心坐标
        float endMinCircleCenterX = (float) (bigCircleCenterX - radius*(1 - Math.tan(Math.toRadians(chamferingAngle))*Math.tan(Math.toRadians((90f - chamferingAngle)/2)))*Math.sin(Math.toRadians(endAngle - chamferingAngle - 91f)));
        float endMinCircleCenterY = (float) (bigCircleCenterY + radius*(1 - Math.tan(Math.toRadians(chamferingAngle))*Math.tan(Math.toRadians((90f - chamferingAngle)/2)))*Math.cos(Math.toRadians(endAngle - chamferingAngle - 91f)));

        //起始小圆外接矩形
        float startMinRectLeft = startMinCircleCenterX - minR;
        float startMinRectTop = startMinCircleCenterY - minR;
        float startMinRectRight = startMinCircleCenterX + minR;
        float startMinRectBottom = startMinCircleCenterY + minR;

        //结束小圆外接矩阵
        float endMinRectLeft = endMinCircleCenterX - minR;
        float endMinRectTop = endMinCircleCenterY - minR;
        float endMinRectRight = endMinCircleCenterX + minR;
        float endMinRectBottom = endMinCircleCenterY + minR;

        //画起始圆角中的扇形
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        rectF.set(maxRectLeft+minR, maxRectTop+ minR, maxRectRight - minR, maxRectBottom-minR);
        canvas.drawArc(rectF,startAngle,chamferingAngle+1,true,mPaint);

        //画起始圆角
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        canvas.drawCircle(startMinCircleCenterX, startMinCircleCenterY, minR, mPaint);


        //画扇形
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        rectF.set(getPaddingLeft()+xDepartAmount,getPaddingTop() + yDepartAmount,getPaddingLeft()+radius*2 + xDepartAmount,getPaddingTop()+radius*2 +yDepartAmount);
        canvas.drawArc(rectF,startAngle+chamferingAngle,sectorAngle - 2*chamferingAngle, true, mPaint);

        //写字
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(deepColor);
        mPaint.setTextSize(textSize);
        canvas.drawText(percentage[i]+"%",(float) ((innerCircleRadius+(radius-innerCircleRadius)/2)*Math.cos(Math.toRadians(midAngle)) + bigCircleCenterX - textSize),
                (float) ((innerCircleRadius+(radius-innerCircleRadius)/2)*Math.sin(Math.toRadians(midAngle)) + bigCircleCenterY + textSize), mPaint );

        //画结束圆角扇形
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        rectF.set(maxRectLeft+minR, maxRectTop+ minR, maxRectRight - minR, maxRectBottom-minR);
        canvas.drawArc(rectF,endAngle-chamferingAngle-1,chamferingAngle,true,mPaint);

        //画结束圆角
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        canvas.drawCircle(endMinCircleCenterX, endMinCircleCenterY, minR, mPaint);


        //描起始圆角中的扇形
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(deepColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawLine((float) (bigCircleCenterX + innerCircleRadius*Math.cos(Math.toRadians(startAngle))), (float) (bigCircleCenterY + innerCircleRadius*Math.sin(Math.toRadians(startAngle))), startMinCircleCutDotX, startMinCircleCutDotY, mPaint);

        //起始圆角描边
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(deepColor);
        rectF.set(startMinRectLeft,startMinRectTop,startMinRectRight,startMinRectBottom);
        canvas.drawArc(rectF,startAngle+chamferingAngle, -(90f+chamferingAngle),false,mPaint);

        //描结束圆角扇形
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(deepColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawLine((float) (bigCircleCenterX + innerCircleRadius * Math.cos(Math.toRadians(endAngle-2))),(float) (bigCircleCenterY+ innerCircleRadius*Math.sin(Math.toRadians(endAngle -2))),endMinCircleCutDotX,endMinCircleCutDotY,mPaint);

        //描边
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(deepColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        rectF.set( getPaddingLeft()+xDepartAmount, getPaddingTop() + yDepartAmount, getPaddingLeft()+radius*2 + xDepartAmount,getPaddingTop()+radius*2 +yDepartAmount);
        canvas.drawArc(rectF,startAngle+chamferingAngle,sectorAngle - 2*chamferingAngle, false, mPaint);

        //结束圆角描边
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(deepColor);
        rectF.set(endMinRectLeft,endMinRectTop,endMinRectRight,endMinRectBottom);
        canvas.drawArc(rectF,endAngle - chamferingAngle, 90f + chamferingAngle,false,mPaint);

        //画空白
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(innerColor);
        mPaint.setAntiAlias(true);
        rectF.set(getPaddingLeft()+xDepartAmount + (radius - innerCircleRadius), getPaddingTop() + yDepartAmount + (radius - innerCircleRadius), getPaddingLeft()+radius*2 + xDepartAmount - (radius -  innerCircleRadius), getPaddingTop()+radius*2 +yDepartAmount - (radius - innerCircleRadius));
        canvas.drawArc(rectF,startAngle,sectorAngle, true, mPaint);

        //画白线
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(innerColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawLine(bigCircleCenterX, bigCircleCenterY , (float) (bigCircleCenterX + innerCircleRadius*Math.cos(Math.toRadians(startAngle))), (float) (bigCircleCenterY + innerCircleRadius*Math.sin(Math.toRadians(startAngle))), mPaint);
        canvas.drawLine(bigCircleCenterX, bigCircleCenterY , (float) (bigCircleCenterX + innerCircleRadius*Math.cos(Math.toRadians(endAngle))), (float) (bigCircleCenterY + innerCircleRadius*Math.sin(Math.toRadians(endAngle))), mPaint);


        //内圆描边
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(deepColor);
        rectF.set(bigCircleCenterX - innerCircleRadius  ,bigCircleCenterY - innerCircleRadius , bigCircleCenterX + innerCircleRadius , bigCircleCenterY + innerCircleRadius );
        canvas.drawArc(rectF,startAngle,sectorAngle-1,false,mPaint);





    }

    public void setPies(Pie[] tmpPies){
        this.pies = tmpPies;
        invalidate();
    }

    public void setInitalColor(int tmpColor){
        this.initalColor = tmpColor;
        invalidate();
    }

    public void setRadius(float tmpRadious){
        this.radius = tmpRadious;
        invalidate();
    }

    public void setChamferingAngle(int tmpChamferingAngle){
        this.chamferingAngle = tmpChamferingAngle;
        invalidate();
    }

    public void setStrokeWidth(int tmpStrokeWidth){
        this.strokeWidth = tmpStrokeWidth;
        invalidate();
    }

    public void setInnerCircleRadius(float tmpInnerCircleRadius){
        this.innerCircleRadius = tmpInnerCircleRadius;
        invalidate();
    }

    private void getPieHeadEnd(){

        double[] pieData = new double[pies.length];
        pieHeadEnds = new PieHeadEnd[pies.length];
        percentage = new String[pies.length];
        double all = 0;

        for(int i = 0; i < pies.length; i++) {
            pieData[i] = (int) pies[i].getNumber();
            all += (int) pies[i].getNumber();
        }

        for(int i = 0; i < pies.length; i++){
            PieHeadEnd phe = new PieHeadEnd();
            if(i == 0){
                phe.setHead(0);
                phe.setEnd((int)((pieData[0]/all) * 360));
                pieHeadEnds[0] = phe;
            }
            if(i > 0) {
                phe.setHead(pieHeadEnds[i - 1].getEnd());
                phe.setEnd((int)(pieHeadEnds[i - 1].getEnd() + (pieData[i] / all) * 360));
                pieHeadEnds[i] = phe;
            }
            if(i == pies.length - 1){
                phe.setHead(pieHeadEnds[i - 1].getEnd());
                phe.setEnd(360);
                pieHeadEnds[i] = phe;
            }
            percentage[i] = String.valueOf((pieData[i]/all) * 100);
            percentage[i] = percentage[i].substring(0,4);
            Log.w("Test","percentage["+i+"]"+percentage[i]);
        }



    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    /**
     *  测量layout宽度
     * */
    private int measureWidth(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        Log.d("TestView", String.valueOf(specSize));

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 400;
            if (specMode == MeasureSpec.AT_MOST){
                //result = Math.min(result, specSize);
                result = (int) (radius * 2) + 2;
            }
        }
        return result;
    }

    /**
     *  测量layout高度
     * */
    private int measureHeight(int measureSpec){

        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        Log.d("TestView", String.valueOf(specSize));

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 400;
            if (specMode == MeasureSpec.AT_MOST){
                //result = Math.min(result, specSize);
                result = (int) (radius * 2) + 2;
            }
        }
        return result;
    }

}
