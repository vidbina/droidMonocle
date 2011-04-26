package com.kingvidbina.test;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;

import java.lang.reflect.Array;

public class ProcessView extends View {
    public static final String TAG = "Test";
    private static final String CLASS = "ProcessView:";
    
    private Context mContext;
    private LayerDrawable mLayerDrawable;
    private Drawable mDrawable;
    private Point mTempPoint;
    private boolean mReady;

    ProcessView(Context context){
	super(context);
	Log.v(TAG, CLASS + "1");
	mContext = context;
    }

    ProcessView(Context context, AttributeSet attrs){
	super(context, attrs);
	Log.v(TAG, CLASS + "2");
	mContext = context;
    }
    
    ProcessView(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	Log.v(TAG, CLASS + "3");
	mContext = context;
    }

    public void setPoints(Point[] points){
	Log.v(TAG, CLASS + "setPoints()");
	prepDrawables(points);
	mReady = true;
	invalidate();
    }

    private void prepDrawables(Point[] targets){
	int len = Array.getLength(targets);
	Log.v(TAG, CLASS + "prepDrawables(): targets: " + len);
	try{
	    Drawable[] drawables;
	    drawables = new Drawable[len];
	    mDrawable = getResources().getDrawable(R.drawable.target);
	    for(int i = 0; i < len; i++){
		int x = targets[i].x;
		int y = targets[i].y;
		drawables[i] = getResources().getDrawable(R.drawable.target);
		drawables[i].setBounds(x, y, x + drawables[i].getIntrinsicHeight(), y + drawables[i].getIntrinsicWidth());
		Log.v(TAG, CLASS + "prepDrawables(): setting x:" + x + " y:" + y + " result[" + i + "]:" + drawables[i].toString());
	    }
	    
	    mLayerDrawable = new LayerDrawable(drawables);
	}catch(Exception e){
	    Log.v(TAG, "prepDrawables(): Exception: " + e.toString());
	}
    }

    protected void onDraw(Canvas canvas){
	super.onDraw(canvas);
	try{
	    if(mReady == true){
		mLayerDrawable.draw(canvas);
	    }
	}catch(Exception e){
	    Log.v(TAG, CLASS + "onDraw(): Exception: " + e.toString());
	}
    }
}
    

   
	