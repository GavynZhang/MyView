package com.gavynzhang.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by GavynZhang on 2016/8/5.
 * 画一个圆
 * 1.wrap_content时，指定半径的两倍即为layout长宽，
 * 2.match_parent时，半径为长宽中较小的一个/2
 */
public class MyView extends View{

    private Paint mPaint;
    private Rect mBounds;

    int color;
    float radious;

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 通过代码调用自定义View时调用
     * */
    public MyView(Context context) {
        super(context);
    }

    /**
     * 通过xml文件调用自定义的View时调用
     */
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyView);
        color = a.getColor(R.styleable.MyView_bac_color,0);
        radious = a.getDimension(R.styleable.MyView_radius,10);

        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();

    }

    public void setColor(int color){
        this.color = color;
        invalidate();
    }

    public void setRadious(int radious) {
        this.radious = radious;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);


        Toast.makeText(getContext(),String.valueOf(getMeasuredWidth()),Toast.LENGTH_SHORT).show();
        canvas.drawCircle((getMeasuredWidth()-getPaddingRight()-getPaddingLeft())/2+getPaddingLeft(),
                (getMeasuredHeight()-getPaddingTop()-getPaddingBottom())/2 + getPaddingTop(),
                          Math.min((getMeasuredWidth()-getPaddingRight()-getPaddingLeft()),(getMeasuredHeight()-getPaddingTop()-getPaddingBottom()))/2,mPaint);
        if(radious != 0){
            Log.d("draw", String.valueOf(getMeasuredHeight()));
            canvas.drawCircle((getMeasuredWidth()-getPaddingRight()-getPaddingLeft())/2+getPaddingLeft(),
                    (getMeasuredHeight()-getPaddingTop()-getPaddingBottom())/2 + getPaddingTop(),
                    radious,mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    /**
     *
     *  测量layout宽度
     *
     * */
    private int measureWidth(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 200;
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
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

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 200;
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
