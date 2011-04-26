package com.kingvidbina.test;

import android.view.View;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.lang.Math;

// TODO: move as much drawing data to xml as possible (colors, dimensions, etc.)
public class HorizonView extends View {
    public static final String TAG = "Test";
    private static final String CLASS = "HorizonView:";

    private Paint mPaint = new Paint();
    private Context mContext;
    private int mOffset;
    private int mLength;
    private Point mCenter;
    private Point mStart;
    private Point mEnd;
    private boolean mReady;
    private boolean mDraw;
    private Math mCalc;
    private int mDefaultAngle;

    HorizonView(Context context){
	super(context);
	mContext = context;
	onConstruct();
    }

    HorizonView(Context context, AttributeSet attrs){
	super(context, attrs);
	mContext = context;
	onConstruct();
    }

    HorizonView(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	mContext = context;
	onConstruct();
    }

    /**
     * set paint
     */
    private void onConstruct(){
	try{
	    mPaint.setColor(mContext.getResources().getColor(R.color.horizon));
	    mPaint.setDither(true);
	    mPaint.setStrokeWidth(5);
	    mPaint.setAlpha(100);
	}catch(Exception e){
	    Log.v(TAG, CLASS + "onConstruct(): Exception: " + e.toString());
	}
    }
    
    /**
     * sets all the necessary fields for drawing the horizonview to the view's canvas
     */
    public void setVitals(Canvas canvas){
	if(mReady != true){
	    mLength = 60;
    	    mCenter = new Point(canvas.getWidth()/2, canvas.getHeight()/2);
	    mStart = new Point(mCenter.x - mLength/2, mCenter.y);
	    mEnd = new Point(mCenter.x + mLength/2, mCenter.y);
	    mDefaultAngle = 270;
	    mReady = true;
	    Log.v(TAG, CLASS + "setVitals(): done");
	}
    }

    protected void onMeasure(int measuredWidth, int measuredHeight){
	super.onMeasure(measuredWidth, measuredHeight);
	setMeasuredDimension(measuredWidth, measuredHeight);
    }

    protected void onDraw(Canvas canvas){
	super.onDraw(canvas);
	try{
	    setVitals(canvas);
	    if(mDraw == true){ canvas.drawLine(mStart.x, mStart.y, mEnd.x, mEnd.y, mPaint); }
	}catch(Exception e){
	    Log.v(TAG, CLASS + "onDraw(): DrawException: " + e.toString());
	}
    }

    /**
     * calculates the points for the horizon line
     */
    protected void setAngle(int angle){
	if(angle != -1){
	    try{
		double x = Math.toRadians(mDefaultAngle - angle);
		float h = (float) mLength/2;
		int o = (int) (Math.sin(x) * h);
		int a = (int) (Math.cos(x) * h);
		mStart.set((int) mCenter.x - a, (int) mCenter.y - o);
		mEnd.set((int) mCenter.x + a, (int) mCenter.y + o);
		mDraw = true;
	    }catch(Exception e){
		Log.v(TAG, CLASS + "setAngle(): Exception: " + e.toString());
	    }
	}else{
	    mDraw = false;
	}
	invalidate();
    }
}